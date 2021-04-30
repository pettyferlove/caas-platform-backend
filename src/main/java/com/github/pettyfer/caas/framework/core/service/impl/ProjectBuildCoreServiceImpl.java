package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.github.pettyfer.caas.framework.biz.service.*;
import com.github.pettyfer.caas.framework.core.event.GitlabPushDetails;
import com.github.pettyfer.caas.framework.core.event.publisher.BuildEventPublisher;
import com.github.pettyfer.caas.framework.core.model.*;
import com.github.pettyfer.caas.framework.core.service.IProjectBuildCoreService;
import com.github.pettyfer.caas.framework.engine.docker.register.service.IHarborService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IJobService;
import com.github.pettyfer.caas.framework.system.entity.SystemMessage;
import com.github.pettyfer.caas.framework.system.event.publisher.MessageEventPublisher;
import com.github.pettyfer.caas.global.constants.*;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.global.properties.BuildImageProperties;
import com.github.pettyfer.caas.utils.LoadBalanceUtil;
import com.github.pettyfer.caas.utils.URLResolutionUtil;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectHook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ProjectBuildCoreServiceImpl implements IProjectBuildCoreService {

    private final BuildImageProperties imageProperties;

    private final IBizGlobalConfigurationService bizGlobalConfigurationService;

    private final IBizUserConfigurationService bizUserConfigurationService;

    private final IBizProjectBuildService bizProjectBuildService;

    private final IBizProjectBuildHistoryService bizProjectBuildHistoryService;

    private final IHarborService harborService;

    private final IBizImagesDepositoryService bizImagesDepositoryService;

    private final IBizUserConfigurationService userConfigurationService;

    private final IBizNamespaceService bizNamespaceService;

    private final IJobService jobService;

    private final BuildEventPublisher publisher;

    private final MessageEventPublisher messagePublisher;

    public ProjectBuildCoreServiceImpl(BuildImageProperties imageProperties, IBizGlobalConfigurationService bizGlobalConfigurationService, IBizUserConfigurationService bizUserConfigurationService, IBizProjectBuildService bizProjectBuildService, IBizProjectBuildHistoryService bizProjectBuildHistoryService, IHarborService harborService, IBizImagesDepositoryService bizImagesDepositoryService, IBizUserConfigurationService userConfigurationService, IBizNamespaceService bizNamespaceService, IJobService jobService, BuildEventPublisher publisher, MessageEventPublisher messagePublisher) {
        this.imageProperties = imageProperties;
        this.bizGlobalConfigurationService = bizGlobalConfigurationService;
        this.bizUserConfigurationService = bizUserConfigurationService;
        this.bizProjectBuildService = bizProjectBuildService;
        this.bizProjectBuildHistoryService = bizProjectBuildHistoryService;
        this.harborService = harborService;
        this.bizImagesDepositoryService = bizImagesDepositoryService;
        this.userConfigurationService = userConfigurationService;
        this.bizNamespaceService = bizNamespaceService;
        this.jobService = jobService;
        this.publisher = publisher;
        this.messagePublisher = messagePublisher;
    }


    @Override
    public IPage<ProjectBuildListView> page(BizProjectBuild projectBuild, Page<BizProjectBuild> page) {
        IPage<ProjectBuildListView> result = new Page<>();
        IPage<ProjectBuildListView> queryPage = bizProjectBuildService.page(projectBuild, page);
        List<ProjectBuildListView> mapList = queryPage.getRecords().stream().peek(i -> {
            if (StrUtil.isNotEmpty(i.getLastState()) && !i.getLastState().equals(BuildStatus.Success.getValue())) {
                Optional<BizNamespace> namespace = Optional.ofNullable(bizNamespaceService.get(i.getNamespaceId()));
                if (namespace.isPresent()) {
                    Job job = jobService.get(namespace.get().getName(), i.getJob());
                    String state = BuildStatus.Building.getValue();
                    if (ObjectUtil.isNotNull(job.getStatus().getActive()) && job.getStatus().getActive() == 1) {
                        state = BuildStatus.Building.getValue();
                    } else if (ObjectUtil.isNotNull(job.getStatus().getFailed()) && job.getStatus().getFailed() == 1) {
                        state = BuildStatus.Fail.getValue();
                    } else if (ObjectUtil.isNotNull(job.getStatus().getSucceeded()) && job.getStatus().getSucceeded() == 1) {
                        state = BuildStatus.Success.getValue();
                    }
                    i.setLastState(state);
                    BizProjectBuildHistory history = new BizProjectBuildHistory();
                    history.setId(i.getHistoryId());
                    history.setBuildStatus(state);
                    bizProjectBuildHistoryService.updateById(history);
                }
            }
        }).collect(Collectors.toList());
        result.setRecords(mapList);
        result.setCurrent(queryPage.getCurrent());
        result.setSize(queryPage.getSize());
        result.setTotal(queryPage.getTotal());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(BizProjectBuild projectBuild) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(projectBuild.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            String id = IdWorker.getIdStr();
            projectBuild.setId(id);
            projectBuild.setImagesDepositoryAlias(namespaceOptional.get().getName());
            if (DepositoryType.GitLabV4.getValue().equals(projectBuild.getDepositoryType())) {
                if (StrUtil.isNotEmpty(projectBuild.getRemoteProjectId())) {
                    String sourceProjectHookId = this.createSourceProjectHook(id, projectBuild.getRemoteProjectId());
                    projectBuild.setProjectHookId(sourceProjectHookId);
                }
            }

            // 创建镜像仓库别名
            String projectId = harborService.createProject(projectBuild.getImagesDepositoryAlias());
            bizImagesDepositoryService.create(projectId, projectBuild.getImagesDepositoryAlias());
            projectBuild.setImagesDepositoryId(projectId);
            bizProjectBuildService.create(projectBuild);
            return id;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean delete(String id) {
        BizProjectBuild bizProjectBuild = bizProjectBuildService.getById(id);
        if (DepositoryType.GitLabV4.getValue().equals(bizProjectBuild.getDepositoryType())) {
            if (StrUtil.isNotEmpty(bizProjectBuild.getRemoteProjectId())) {
                this.removeSourceProjectHook(bizProjectBuild.getRemoteProjectId(), bizProjectBuild.getProjectHookId());
            }
        }
        return bizProjectBuildService.delete(id);
    }


    @Override
    public void manualBuild(String id) {
        startBuild(id);
    }

    @Override
    public void autoBuild(String id, GitlabPushDetails pushDetails) {
        LambdaQueryWrapper<BizProjectBuild> queryWrapper = Wrappers.<BizProjectBuild>lambdaQuery()
                .eq(BizProjectBuild::getId, id)
                .eq(BizProjectBuild::getDelFlag, 0)
                .eq(BizProjectBuild::getOpenAutoBuild, 1);
        Optional<BizProjectBuild> projectBuildOptional = Optional.ofNullable(bizProjectBuildService.getOne(queryWrapper));
        if (projectBuildOptional.isPresent()) {
            BizProjectBuild bizProjectBuild = projectBuildOptional.get();
            if (pushDetails.getRef().contains(bizProjectBuild.getRemoteBranch())) {
                startBuild(id);
            }
        }
    }

    @Override
    @SneakyThrows
    public String createSourceProjectHook(String id, String sourceProjectId) {
        UserConfiguration userConfiguration = bizUserConfigurationService.loadConfig();
        GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
        GitlabAPI connect = GitlabAPI.connect(userConfiguration.getGitlabHomePath(), userConfiguration.getGitlabApiToken());
        String server = LoadBalanceUtil.chooseServer(globalConfiguration.getClusterServer());
        String projectHookUrl = StrUtil.format("http://{}/api/v1/hooks/gitlab/", server);
        GitlabProject project = connect.getProject(sourceProjectId);
        List<GitlabProjectHook> projectHooks = connect.getProjectHooks(project);
        List<GitlabProjectHook> collect = projectHooks.stream().filter(i -> projectHookUrl.equals(i.getUrl())).collect(Collectors.toList());
        if (collect.size() == 0) {
            GitlabProjectHook hook = connect.addProjectHook(project, projectHookUrl);
            return hook.getId();
        } else {
            log.warn("There is a trigger with the same ID, please check the reason, Project_ID is {}, Hook_ID is{}", sourceProjectId, collect.get(0).getId());
            return collect.get(0).getId();
        }
    }

    @Override
    public void removeSourceProjectHook(String sourceProjectId, String sourceProjectHookId) {
        try {
            UserConfiguration userConfiguration = bizUserConfigurationService.loadConfig();
            GitlabAPI connect = GitlabAPI.connect(userConfiguration.getGitlabHomePath(), userConfiguration.getGitlabApiToken());
            GitlabProjectHook hook = new GitlabProjectHook();
            hook.setId(sourceProjectHookId);
            hook.setProjectId(Integer.valueOf(sourceProjectId));
            connect.deleteProjectHook(hook);
        } catch (Exception e) {
            log.warn("Trigger has been removed!");
        }
    }

    @Override
    public String createImagesDepositoryProject() {
        return null;
    }

    @Override
    public void removeImagesDepositoryProject() {

    }

    @Override
    public List<ProjectBuildHistorySelectView> historySelect(String id) {
        return bizProjectBuildHistoryService.historySelect(id);
    }

    @Override
    public IPage<BizProjectBuildHistory> page(String buildId, BizProjectBuildHistory history, Page<BizProjectBuildHistory> page) {
        history.setBuildId(buildId);
        return bizProjectBuildHistoryService.page(history, page);
    }

    @Override
    public BuildStepView logStep(String buildId, String jobId) {
        BizProjectBuild bizProjectBuild = bizProjectBuildService.get(buildId);
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizProjectBuild.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            BuildStepView result = new BuildStepView();
            Pod pod = jobService.loadPod(namespaceOptional.get().getName(), jobId);
            if (ObjectUtil.isNotNull(pod)) {
                result.setStep(pod.getStatus().getInitContainerStatuses());
                result.setPodName(pod.getMetadata().getName());
                result.setNamespaceId(bizProjectBuild.getNamespaceId());
            }
            return result;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public Boolean deleteHistory(String historyId) {
        BizProjectBuildHistory history = bizProjectBuildHistoryService.get(historyId);
        BizProjectBuild projectBuild = bizProjectBuildService.get(history.getBuildId());
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(projectBuild.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            jobService.delete(namespaceOptional.get().getName(), history.getJobId());
            bizProjectBuildHistoryService.delete(history.getId());
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public void updateStatus(String jobId, BuildStatus status) {
        SystemMessage buildMessage = new SystemMessage();
        buildMessage.setBusinessName("build");
        buildMessage.setMessage("构建通知");
        buildMessage.setDeliver("system");
        buildMessage.setTime(LocalDateTime.now());
        LambdaUpdateWrapper<BizProjectBuildHistory> updateWrapper = Wrappers.<BizProjectBuildHistory>lambdaUpdate();
        updateWrapper.set(BizProjectBuildHistory::getBuildStatus, status.getValue());
        updateWrapper.eq(BizProjectBuildHistory::getJobId, jobId);
        if (bizProjectBuildHistoryService.update(updateWrapper)) {
            LambdaQueryWrapper<BizProjectBuildHistory> queryWrapper = Wrappers.<BizProjectBuildHistory>lambdaQuery();
            queryWrapper.eq(BizProjectBuildHistory::getJobId, jobId);
            BizProjectBuildHistory history = bizProjectBuildHistoryService.getOne(queryWrapper);
            BizProjectBuild projectBuild = bizProjectBuildService.get(history.getBuildId());
            if (status == BuildStatus.Success) {
                LambdaQueryWrapper<BizProjectBuild> parentQuery = Wrappers.<BizProjectBuild>lambdaQuery();
                parentQuery.eq(BizProjectBuild::getParentId, projectBuild.getId());
                parentQuery.eq(BizProjectBuild::getDelFlag, 0);
                parentQuery.eq(BizProjectBuild::getOpenAutoBuild, 1);
                List<BizProjectBuild> projectBuilds = bizProjectBuildService.list(parentQuery);
                for (BizProjectBuild build:projectBuilds) {
                    this.startBuild(build.getId());
                }
                buildMessage.setType("success");
                buildMessage.setContent(projectBuild.getProjectName() + "自动构建成功");
            } else {
                buildMessage.setType("error");
                buildMessage.setContent(projectBuild.getProjectName() + "自动构建失败，请检查日志");
            }
            buildMessage.setReceiver(projectBuild.getCreator());
            if (StrUtil.isNotEmpty(history.getImageFullName())) {
                String[] image = history.getImageFullName().split(":");
                if (image.length == 2) {
                    publisher.push(history.getBuildId(), image[0], image[1]);
                }
            }
            messagePublisher.push(buildMessage);
        }
    }

    public Boolean startBuild(String id) {
        Optional<BizProjectBuild> buildOptional = Optional.ofNullable(bizProjectBuildService.get(id));
        if (buildOptional.isPresent()) {
            String jobName = "build-" + UUID.randomUUID().toString();
            BizProjectBuild projectBuild = buildOptional.get();
            UserConfiguration userConfiguration = userConfigurationService.loadConfig(projectBuild.getCreator());
            GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
            Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(projectBuild.getNamespaceId()));
            if (bizNamespaceOptional.isPresent()) {
                BizNamespace namespace = bizNamespaceOptional.get();
                Map<String, String> env = new HashMap<>();
                env.put("PRIVATE_KEY", userConfiguration.getPrivateKey().replaceAll("\n", "\\\\n"));
                env.put("GIT_ROOT", "gitlab.ggjs.sinobest.cn");
                env.put("BUILD_CODE_PATH", GlobalConstant.BUILD_CODE_PATH);
                env.put("REMOTE_PATH", projectBuild.getRemotePath());
                env.put("REMOTE_BRANCH", projectBuild.getRemoteBranch());
                env.put("PROJECT_NAME", projectBuild.getProjectName());
                env.put("BUILD_TARGET_PATH", projectBuild.getBuildTargetPath());
                env.put("BUILD_TARGET_NAME", projectBuild.getBuildTargetName());

                if(DepositoryType.Subversion.getValue().equals(projectBuild.getDepositoryType())){
                    Preconditions.checkNotNull(userConfiguration.getSubversionUsername(), "未配置SVN账号，请前往个人配置页面配置");
                    Preconditions.checkNotNull(userConfiguration.getSubversionPassword(), "未配置SVN密码，请前往个人配置页面配置");
                    env.put("USERNAME", userConfiguration.getSubversionUsername());
                    env.put("PASSWORD", userConfiguration.getSubversionPassword());
                }

                env.put("JOB_NAME", jobName);
                env.put("FILE_ID", jobName);
                env.put("USER_ID", projectBuild.getCreator());
                env.put("NOTIFICATION_FLAG", "project");
                env.put("REMOTE_SERVER", LoadBalanceUtil.chooseServer(globalConfiguration.getClusterServer()));
                if(projectBuild.getLinkProject()){
                    BizProjectBuild parentProject = bizProjectBuildService.get(projectBuild.getParentId());
                    Optional<BizProjectBuildHistory> historyOptional = Optional.ofNullable(bizProjectBuildHistoryService.queryLastBuild(parentProject.getId()));
                    historyOptional.ifPresent(bizProjectBuildHistory -> env.put("PARENT_PROJECT_LAST_BUILD_FILE", bizProjectBuildHistory.getFileId()));
                }
                env.put("PRE_SHELL_SCRIPT", projectBuild.getPreShellScript());
                env.put("POST_SHELL_SCRIPT", projectBuild.getPostShellScript());

                StringBuilder imageName = new StringBuilder();
                String tagName = String.valueOf(System.currentTimeMillis());
                Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
                String ip = URLResolutionUtil.ip(globalConfiguration.getDockerRegistryPath());
                String port = URLResolutionUtil.port(globalConfiguration.getDockerRegistryPath());
                imageName.append(ip);
                if (!"80".equals(port) && !"443".equals(port)) {
                    imageName.append(":")
                            .append(port);
                }
                imageName.append("/").append(projectBuild.getImagesDepositoryAlias())
                        .append("/")
                        .append(projectBuild.getProjectName());

                env.put("DOCKER_REGISTRY_USERNAME", globalConfiguration.getDockerRegistryUsername());
                env.put("DOCKER_REGISTRY_PASSWORD", globalConfiguration.getDockerRegistryPassword());
                env.put("DOCKER_REGISTRY_PATH", ip);
                env.put("DOCKERFILE_CONTENT", StrUtil.isNotEmpty(projectBuild.getDockerfileContent()) ? projectBuild.getDockerfileContent().replaceAll("\n", "\\\\n") : "");
                env.put("DOCKER_IMAGE_NAME", imageName.toString());
                env.put("DOCKER_IMAGE_TAG", tagName);
                env.put("DOCKERFILE_EXIST", projectBuild.getDockerfileAlreadyExists() == 1 ? "yes" : "no");
                env.put("DOCKERFILE_PATH", projectBuild.getDockerfilePath());
                env.put("BUILD_PARAMS", projectBuild.getBuildParams());
                env.put("BUILD_COMMAND", projectBuild.getBuildCommand());
                env.put("BUILD_TOOL", projectBuild.getBuildTool());


                String envType = EnvConstant.transform(projectBuild.getEnvType());
                List<VolumeMount> volumeMounts = fetchVolumeMount(projectBuild.getNeedBuildProject()==1, projectBuild.getBuildTool(), namespace.getName());
                List<Volume> volumes = fetchVolume(projectBuild.getNeedBuildProject()==1, projectBuild.getBuildTool(), namespace.getName());

                Job job = new JobBuilder()
                        .withNewMetadata()
                        .withName(jobName)
                        .endMetadata()
                        .withNewSpec()
                        .withActiveDeadlineSeconds(1800L)
                        .withTtlSecondsAfterFinished(2592000)
                        .withBackoffLimit(0)
                        .withNewTemplate()
                        .withNewMetadata()
                        .withLabels(fetchBuildLabel(jobName, envType))
                        .endMetadata()
                        .withNewSpec()
                        .withInitContainers(fetchInitContainer(env, volumeMounts, projectBuild))
                        .withContainers(fetchContainer(env))
                        .withVolumes(volumes)
                        .withRestartPolicy("Never")
                        .addNewImagePullSecret(namespace.getRegistrySecretName())
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();
                jobService.create(namespace.getName(), job);
                BizProjectBuildHistory history = new BizProjectBuildHistory();
                history.setBuildId(projectBuild.getId());
                history.setJobId(jobName);
                history.setBuildStatus(BuildStatus.Send.getValue());
                if (projectBuild.getPersistentBuildFile() == 1) {
                    history.setFileId(jobName);
                }
                if (projectBuild.getNeedBuildImage() == 1) {
                    history.setImageFullName(imageName.append(":").append(tagName).toString());
                }
                bizProjectBuildHistoryService.create(history);
            } else {
                throw new BaseRuntimeException("命名空间不存在");
            }
        } else {
            throw new BaseRuntimeException("构建项目不存在");
        }

        return true;
    }


    private Map<String, String> fetchBuildLabel(String value, String envType) {
        Map<String, String> label = new HashMap<>();
        label.put(KubernetesConstant.BUILD_LABEL, value);
        label.put(KubernetesConstant.ENVIRONMENT_LABEL, envType);
        return label;
    }

    private List<EnvVar> fetchEnv(Map<String, String> env) {
        List<EnvVar> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            list.add(new EnvVarBuilder()
                    .withName(entry.getKey())
                    .withValue(entry.getValue())
                    .build());
        }
        return list;
    }

    private List<Volume> fetchVolume(Boolean needBuild, String buildTool, String name) {
        List<Volume> volumes = new ArrayList<>();
        volumes.add(new VolumeBuilder()
                .withName("work-dir")
                .withEmptyDir(new EmptyDirVolumeSource())
                .build());
        if(needBuild) {
            String cacheHostDir = "";
            switch (buildTool) {
                case "maven":
                    cacheHostDir = "/usr/build/cache/" + name + "/maven";
                    break;
                case "npm":
                case "yarn":
                    cacheHostDir = "/usr/build/cache/" + name + "/nodejs";
                    break;
                default:
                    throw new BaseRuntimeException("请指定构建工具");
            }
            volumes.add(new VolumeBuilder()
                    .withName("cache-dir")
                    .withHostPath(new HostPathVolumeSourceBuilder()
                            .withPath(cacheHostDir)
                            .withType("DirectoryOrCreate")
                            .build())
                    .build());
        }
        volumes.add(new VolumeBuilder()
                .withName("docker-dir")
                .withHostPath(new HostPathVolumeSourceBuilder()
                        .withPath("/var/run/docker.sock")
                        .build())
                .build());
        return volumes;
    }

    private List<VolumeMount> fetchVolumeMount(Boolean needBuild, String buildTool, String name) {
        List<VolumeMount> volumeMounts = new ArrayList<>();
        volumeMounts.add(new VolumeMountBuilder()
                .withName("work-dir")
                .withMountPath(GlobalConstant.BUILD_CODE_PATH)
                .build());
        if(needBuild) {
            String cacheDir = "";
            switch (buildTool) {
                case "maven":
                    cacheDir = "/usr/cache/maven";
                    break;
                case "npm":
                case "yarn":
                    cacheDir = "/usr/cache/nodejs";
                    break;
                default:
                    throw new BaseRuntimeException("请指定构建工具");
            }
            volumeMounts.add(new VolumeMountBuilder()
                    .withName("cache-dir")
                    .withMountPath(cacheDir)
                    .build());
        }
        volumeMounts.add(new VolumeMountBuilder()
                .withName("docker-dir")
                .withMountPath("/var/run/docker.sock")
                .build());
        return volumeMounts;
    }

    private List<Container> fetchInitContainer(Map<String,
            String> env
            , List<VolumeMount> volumeMounts
            , BizProjectBuild projectBuild) {

        List<Container> containers = new LinkedList<>();
        if (DepositoryType.Subversion.getValue().equals(projectBuild.getDepositoryType())) {
            containers.add(new ContainerBuilder()
                    .withName("svn-pull")
                    .withImage(imageProperties.getImages().get("svn-pull"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(volumeMounts)
                    .build());
        } else if(DepositoryType.GitLabV4.getValue().equals(projectBuild.getDepositoryType())) {
            containers.add(new ContainerBuilder()
                    .withName("git-pull")
                    .withImage(imageProperties.getImages().get("git-pull"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(volumeMounts)
                    .build());
        } else {
            throw new BaseRuntimeException("不支持的仓库类型");
        }

        if(projectBuild.getRunPreShellScript()) {
            containers.add(new ContainerBuilder()
                    .withName("pre-shell")
                    .withImage(imageProperties.getImages().get("shell"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .addNewEnv()
                    .withName("SHELL_TYPE")
                    .withValue("PRE")
                    .endEnv()
                    .withVolumeMounts(volumeMounts)
                    .build());
        }

        if (projectBuild.getNeedBuildProject() == 1) {
            switch (projectBuild.getBuildTool()) {
                case "maven":
                    containers.add(new ContainerBuilder()
                            .withName("maven-build")
                            .withImage(imageProperties.getImages().get("maven-build"))
                            .withImagePullPolicy("Always")
                            .withEnv(fetchEnv(env))
                            .withVolumeMounts(volumeMounts)
                            .build());
                    break;
                case "npm":
                case "yarn":
                    containers.add(new ContainerBuilder()
                            .withName("nodejs-build")
                            .withImage(imageProperties.getImages().get("nodejs-build"))
                            .withImagePullPolicy("Always")
                            .withEnv(fetchEnv(env))
                            .withVolumeMounts(volumeMounts)
                            .build());
                    break;
                default:
                    throw new BaseRuntimeException("请指定构建工具");
            }
        }

        if(projectBuild.getRunPostShellScript()) {
            containers.add(new ContainerBuilder()
                    .withName("post-shell")
                    .withImage(imageProperties.getImages().get("shell"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .addNewEnv()
                    .withName("SHELL_TYPE")
                    .withValue("POST")
                    .endEnv()
                    .withVolumeMounts(volumeMounts)
                    .build());
        }

        if (projectBuild.getPersistentBuildFile() == 1) {
            containers.add(new ContainerBuilder()
                    .withName("persistence")
                    .withImage(imageProperties.getImages().get("persistence"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(volumeMounts)
                    .build());
        }

        if (projectBuild.getNeedBuildImage() == 1) {
            containers.add(new ContainerBuilder()
                    .withName("docker-build")
                    .withImage(imageProperties.getImages().get("docker-build"))
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(volumeMounts)
                    .build());
        }

        return containers;
    }

    private List<Container> fetchContainer(Map<String, String> env) {
        List<Container> containers = new LinkedList<>();
        containers.add(new ContainerBuilder()
                .withName("notification")
                .withImage(imageProperties.getImages().get("notification"))
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .build());
        return containers;
    }

}
