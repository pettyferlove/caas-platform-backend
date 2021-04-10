package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.biz.service.IBizSqlBuildHistoryService;
import com.github.pettyfer.caas.framework.biz.service.IBizSqlBuildService;
import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IJobService;
import com.github.pettyfer.caas.global.constants.BuildStatus;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.GlobalConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.framework.core.model.BuildStepView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;
import com.github.pettyfer.caas.framework.core.model.UserConfiguration;
import com.github.pettyfer.caas.framework.core.service.ISqlBuildCoreService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class SqlBuildCoreServiceImpl implements ISqlBuildCoreService {

    private final Environment environment;

    private final IBizSqlBuildService bizSqlBuildService;

    private final IBizSqlBuildHistoryService bizSqlBuildHistoryService;

    private final IBizNamespaceService bizNamespaceService;

    private final IBizUserConfigurationService userConfigurationService;

    private final IJobService jobService;

    public SqlBuildCoreServiceImpl(Environment environment, IBizSqlBuildService bizSqlBuildService, IBizSqlBuildHistoryService bizSqlBuildHistoryService, IBizNamespaceService bizNamespaceService, IBizUserConfigurationService userConfigurationService, IJobService jobService) {
        this.environment = environment;
        this.bizSqlBuildService = bizSqlBuildService;
        this.bizSqlBuildHistoryService = bizSqlBuildHistoryService;
        this.bizNamespaceService = bizNamespaceService;
        this.userConfigurationService = userConfigurationService;
        this.jobService = jobService;
    }

    @Override
    public IPage<SqlBuildListView> page(BizSqlBuild sqlBuild, Page<BizSqlBuild> page) {
        IPage<SqlBuildListView> result = new Page<>();
        IPage<SqlBuildListView> queryPage = bizSqlBuildService.page(sqlBuild, page);
        List<SqlBuildListView> mapList = queryPage.getRecords().stream().peek(i -> {
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
                    BizSqlBuildHistory history = new BizSqlBuildHistory();
                    history.setId(i.getHistoryId());
                    history.setBuildStatus(state);
                    bizSqlBuildHistoryService.update(history);
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
    public BizSqlBuild get(String id) {
        return bizSqlBuildService.get(id);
    }

    @Override
    public Boolean update(BizSqlBuild sqlBuild) {
        return bizSqlBuildService.update(sqlBuild);
    }

    @Override
    public String create(BizSqlBuild sqlBuild) {
        return bizSqlBuildService.create(sqlBuild);
    }

    @Override
    public Boolean startBuild(String id) {
        Optional<BizSqlBuild> buildOptional = Optional.ofNullable(bizSqlBuildService.get(id));
        if (buildOptional.isPresent()) {
            String jobName = "build-" + UUID.randomUUID().toString();
            BizSqlBuild sqlBuild = buildOptional.get();
            UserConfiguration userConfiguration = userConfigurationService.loadConfig(sqlBuild.getCreator());
            Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(sqlBuild.getNamespaceId()));
            if (bizNamespaceOptional.isPresent()) {
                BizNamespace namespace = bizNamespaceOptional.get();
                Map<String, String> env = new HashMap<>();
                env.put("PRIVATE_KEY", userConfiguration.getPrivateKey().replaceAll("\n", "\\\\n"));
                env.put("GIT_ROOT", "gitlab.ggjs.sinobest.cn");
                env.put("BUILD_CODE_PATH", GlobalConstant.BUILD_CODE_PATH);
                env.put("REMOTE_PATH", sqlBuild.getRemotePath());
                env.put("REMOTE_BRANCH", sqlBuild.getRemoteBranch());
                env.put("PROJECT_NAME", sqlBuild.getProjectName());
                env.put("BUILD_TARGET_PATH", "");
                env.put("BUILD_TARGET_NAME", "build");
                env.put("USERNAME", sqlBuild.getUsername());
                env.put("PASSWORD", sqlBuild.getPassword());
                env.put("JOB_NAME", jobName);
                env.put("CONFIG", sqlBuild.getConfig());
                env.put("FILE_ID", jobName);
                env.put("USER_ID", sqlBuild.getCreator());
                env.put("FULL", sqlBuild.getFull() ? "yes" : "no");
                env.put("FORM", sqlBuild.getSqlFrom());
                env.put("TO", sqlBuild.getSqlTo());
                env.put("NOTIFICATION_FLAG", "sql");
                try {
                    env.put("REMOTE_SERVER", InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("local.server.port"));
                } catch (Exception e) {
                    throw new BaseRuntimeException("无法获取服务器IP");
                }

                String envType = EnvConstant.transform(sqlBuild.getEnvType());

                Job job = new JobBuilder()
                        .withNewMetadata()
                        .withName(jobName)
                        .endMetadata()
                        .withNewSpec()
                        .withBackoffLimit(0)
                        .withNewTemplate()
                        .withNewMetadata()
                        .withLabels(fetchBuildLabel(jobName, envType))
                        .endMetadata()
                        .withNewSpec()
                        .withInitContainers(fetchInitContainer(env, sqlBuild.getDepositoryType()))
                        .withContainers(fetchContainer(env))
                        .withVolumes(fetchVolume())
                        .withRestartPolicy("Never")
                        .addNewImagePullSecret(namespace.getRegistrySecretName())
                        .endSpec()
                        .endTemplate()
                        .endSpec()
                        .build();
                jobService.create(namespace.getName(), job);
                BizSqlBuildHistory history = new BizSqlBuildHistory();
                history.setBuildId(sqlBuild.getId());
                history.setJobId(jobName);
                history.setBuildStatus(BuildStatus.Send.getValue());
                history.setStart(sqlBuild.getSqlFrom());
                history.setEnd(sqlBuild.getSqlTo());
                history.setFileId(jobName);
                bizSqlBuildHistoryService.create(history);
            } else {
                throw new BaseRuntimeException("命名空间不存在");
            }
        } else {
            throw new BaseRuntimeException("构建项目不存在");
        }

        return true;
    }

    @Override
    public List<SqlBuildHistorySelectView> historySelect(String id) {
        return bizSqlBuildHistoryService.historySelect(id);
    }

    @Override
    public void updateStatus(String jobId, BuildStatus status) {
        LambdaUpdateWrapper<BizSqlBuildHistory> updateWrapper = Wrappers.<BizSqlBuildHistory>lambdaUpdate();
        updateWrapper.set(BizSqlBuildHistory::getBuildStatus, status.getValue());
        updateWrapper.eq(BizSqlBuildHistory::getJobId, jobId);
        bizSqlBuildHistoryService.update(updateWrapper);
    }

    @Override
    public IPage<BizSqlBuildHistory> page(String buildId, BizSqlBuildHistory history, Page<BizSqlBuildHistory> page) {
        history.setBuildId(buildId);
        return bizSqlBuildHistoryService.page(history, page);
    }

    @Override
    public BuildStepView logStep(String buildId, String jobId) {
        BizSqlBuild bizSqlBuild = bizSqlBuildService.get(buildId);
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizSqlBuild.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            BuildStepView result = new BuildStepView();
            Pod pod = jobService.loadPod(namespaceOptional.get().getName(), jobId);
            if (ObjectUtil.isNotNull(pod)) {
                result.setStep(pod.getStatus().getInitContainerStatuses());
                result.setPodName(pod.getMetadata().getName());
                result.setNamespaceId(bizSqlBuild.getNamespaceId());
            }
            return result;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deleteHistory(String historyId) {
        BizSqlBuildHistory history = bizSqlBuildHistoryService.get(historyId);
        BizSqlBuild sqlBuild = bizSqlBuildService.get(history.getBuildId());
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(sqlBuild.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            jobService.delete(namespaceOptional.get().getName(), history.getJobId());
            bizSqlBuildHistoryService.delete(history.getId());
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    private Map<String, String> fetchBuildLabel(String value, String envType) {
        Map<String, String> label = new HashMap<>();
        label.put("build", value);
        label.put("environment", envType);
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

    private List<Volume> fetchVolume() {
        List<Volume> volumes = new ArrayList<>();
        volumes.add(new VolumeBuilder()
                .withName("work-dir")
                .withEmptyDir(new EmptyDirVolumeSource())
                .build());
        return volumes;
    }

    private List<VolumeMount> fetchVolumeMount() {
        List<VolumeMount> volumeMounts = new ArrayList<>();
        volumeMounts.add(new VolumeMountBuilder()
                .withName("work-dir")
                .withMountPath(GlobalConstant.BUILD_CODE_PATH)
                .build());
        return volumeMounts;
    }

    private List<Container> fetchInitContainer(Map<String, String> env, Integer depositoryType) {
        List<Container> containers = new LinkedList<>();
        if (depositoryType == 2) {
            containers.add(new ContainerBuilder()
                    .withName("svn-pull")
                    .withImage("192.168.13.61/tools/build-subversion-tool:1.0.0")
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(fetchVolumeMount())
                    .build());
        } else {
            containers.add(new ContainerBuilder()
                    .withName("git-pull")
                    .withImage("192.168.13.61/tools/build-git-tool:1.0.0")
                    .withImagePullPolicy("Always")
                    .withEnv(fetchEnv(env))
                    .withVolumeMounts(fetchVolumeMount())
                    .build());
        }

        containers.add(new ContainerBuilder()
                .withName("sql-build")
                .withImage("192.168.13.61/tools/build-sql-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(fetchVolumeMount())
                .build());
        containers.add(new ContainerBuilder()
                .withName("persistence")
                .withImage("192.168.13.61/tools/build-persistence-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(fetchVolumeMount())
                .build());
        return containers;
    }

    private List<Container> fetchContainer(Map<String, String> env) {
        List<Container> containers = new LinkedList<>();
        containers.add(new ContainerBuilder()
                .withName("notification")
                .withImage("192.168.13.61/tools/build-notification-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(fetchVolumeMount())
                .build());
        return containers;
    }

}
