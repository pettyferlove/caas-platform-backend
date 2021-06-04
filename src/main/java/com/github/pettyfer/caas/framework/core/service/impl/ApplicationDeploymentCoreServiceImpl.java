package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.*;
import com.github.pettyfer.caas.framework.biz.service.*;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentListView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentMountView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentPortView;
import com.github.pettyfer.caas.framework.core.service.IApplicationDeploymentCoreService;
import com.github.pettyfer.caas.framework.core.service.IKeywordCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.constants.RunStatus;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.YamlUtil;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ApplicationDeploymentCoreServiceImpl implements IApplicationDeploymentCoreService {


    private final IBizApplicationDeploymentService bizApplicationDeploymentService;

    private final IBizServiceDiscoveryService bizServiceDiscoveryService;

    private final IBizApplicationDeploymentMountService bizApplicationDeploymentVolumeService;

    private final IBizConfigService bizConfigService;

    private final IBizPersistentStorageService bizPersistentStorageService;

    private final IBizNamespaceService bizNamespaceService;

    private final IDeploymentService deploymentService;

    private final INetworkService networkService;

    private final IKeywordCoreService keywordCoreService;

    public ApplicationDeploymentCoreServiceImpl(IBizApplicationDeploymentService bizApplicationDeploymentService, IBizServiceDiscoveryService bizServiceDiscoveryService, IBizApplicationDeploymentMountService bizApplicationDeploymentVolumeService, IBizConfigService bizConfigService, IBizPersistentStorageService bizPersistentStorageService, IDeploymentService deploymentService, IBizNamespaceService bizNamespaceService, INetworkService networkService, IKeywordCoreService keywordCoreService) {
        this.bizApplicationDeploymentService = bizApplicationDeploymentService;
        this.bizServiceDiscoveryService = bizServiceDiscoveryService;
        this.bizApplicationDeploymentVolumeService = bizApplicationDeploymentVolumeService;
        this.bizConfigService = bizConfigService;
        this.bizPersistentStorageService = bizPersistentStorageService;
        this.deploymentService = deploymentService;
        this.bizNamespaceService = bizNamespaceService;
        this.networkService = networkService;
        this.keywordCoreService = keywordCoreService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(String namespaceId, ApplicationDeploymentDetailView deploymentDetail) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            try {
                BizApplicationDeployment bizApplicationDeployment = new BizApplicationDeployment();
                ConverterUtil.convert(deploymentDetail, bizApplicationDeployment);
                bizApplicationDeployment.setRunStatus(RunStatus.Preparing.getValue());
                String deploymentId = bizApplicationDeploymentService.create(namespaceId, bizApplicationDeployment);

                keywordCoreService.map(deploymentDetail.getKeywords(), deploymentId, "application_deployment");

                BizServiceDiscovery bizServiceDiscovery = new BizServiceDiscovery();
                ConverterUtil.convert(deploymentDetail, bizServiceDiscovery);
                bizServiceDiscovery.setNamespaceId(namespaceId);
                bizServiceDiscovery.setDeploymentId(deploymentId);
                bizServiceDiscovery.setName(deploymentDetail.getName());
                bizServiceDiscovery.setMatchLabel(JSON.toJSONString(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()))));
                bizServiceDiscoveryService.create(bizServiceDiscovery);
                bizApplicationDeploymentVolumeService.batchInsert(deploymentId, deploymentDetail.getMounts());

                Deployment deployment = buildDeployment(namespaceOptional.get(), deploymentDetail);
                Optional<Deployment> optionalDeployment = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                if (optionalDeployment.isPresent()) {
                    deploymentService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), deployment);
                } else {
                    deploymentService.create(namespaceOptional.get().getName(), deployment);
                }
                if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                    Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                    if (serviceOptional.isPresent()) {
                        networkService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), buildService(deploymentDetail));
                    } else {
                        networkService.create(namespaceOptional.get().getName(), buildService(deploymentDetail));
                    }
                }

                return deploymentId;
            } catch (DuplicateKeyException e) {
                throw new BaseRuntimeException("已存在同名部署");
            }
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }

    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean update(String namespaceId, ApplicationDeploymentDetailView deploymentDetail) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            try {
                String deploymentId = deploymentDetail.getId();
                BizApplicationDeployment bizApplicationDeployment = new BizApplicationDeployment();
                ConverterUtil.convert(deploymentDetail, bizApplicationDeployment);
                bizApplicationDeployment.setRunStatus(RunStatus.Updating.getValue());
                Boolean update = bizApplicationDeploymentService.update(namespaceId, bizApplicationDeployment);

                keywordCoreService.map(deploymentDetail.getKeywords(), deploymentId, "application_deployment");

                if (update) {
                    int count = bizServiceDiscoveryService.count(Wrappers.<BizServiceDiscovery>lambdaQuery().eq(BizServiceDiscovery::getDeploymentId, deploymentId).eq(BizServiceDiscovery::getDelFlag, 0));
                    BizServiceDiscovery bizServiceDiscovery = new BizServiceDiscovery();
                    ConverterUtil.convert(deploymentDetail, bizServiceDiscovery);
                    bizServiceDiscovery.setId(null);
                    bizServiceDiscovery.setNamespaceId(namespaceId);
                    bizServiceDiscovery.setDeploymentId(deploymentId);
                    bizServiceDiscovery.setName(deploymentDetail.getName());
                    bizServiceDiscovery.setMatchLabel(JSON.toJSONString(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()))));
                    if (count > 0) {
                        LambdaUpdateWrapper<BizServiceDiscovery> updateWrapper = Wrappers.<BizServiceDiscovery>lambdaUpdate();
                        updateWrapper.eq(BizServiceDiscovery::getDeploymentId, deploymentId);
                        updateWrapper.eq(BizServiceDiscovery::getDelFlag, 0);
                        bizServiceDiscoveryService.update(bizServiceDiscovery, updateWrapper);
                    } else {
                        bizServiceDiscoveryService.create(bizServiceDiscovery);
                    }

                    bizApplicationDeploymentVolumeService.remove(Wrappers.<BizApplicationDeploymentMount>lambdaQuery().eq(BizApplicationDeploymentMount::getDeploymentId, deploymentId));
                    bizApplicationDeploymentVolumeService.batchInsert(deploymentId, deploymentDetail.getMounts());
                }

                Deployment deployment = buildDeployment(namespaceOptional.get(), deploymentDetail);
                Optional<Deployment> optionalDeployment = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                if (optionalDeployment.isPresent()) {
                    deploymentService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), deployment);
                } else {
                    deploymentService.create(namespaceOptional.get().getName(), deployment);
                }

                Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                if (serviceOptional.isPresent()) {
                    if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                        networkService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), buildService(deploymentDetail));
                    } else {
                        networkService.delete(namespaceOptional.get().getName(), deploymentDetail.getName());
                    }
                } else {
                    networkService.create(namespaceOptional.get().getName(), buildService(deploymentDetail));
                }

                return update;
            } catch (DuplicateKeyException e) {
                throw new BaseRuntimeException("已存在同名部署");
            }
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }

    }

    @Override
    public IPage<ApplicationDeploymentListView> page(String namespaceId, ApplicationDeploymentListView applicationDeploymentListView, Page<ApplicationDeploymentListView> page) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            BizApplicationDeployment bizApplicationDeployment = new BizApplicationDeployment();
            Page<BizApplicationDeployment> bizApplicationDeploymentPage = new Page<>();
            ConverterUtil.convert(applicationDeploymentListView, bizApplicationDeployment);
            ConverterUtil.convert(page, bizApplicationDeploymentPage);
            IPage<ApplicationDeploymentListView> result = new Page<>();
            IPage<BizApplicationDeployment> queryPage = bizApplicationDeploymentService.page(namespaceId, bizApplicationDeployment, bizApplicationDeploymentPage);
            List<BizApplicationDeployment> records = queryPage.getRecords();
            List<ApplicationDeploymentListView> mapList = records.stream().map(i -> {
                ApplicationDeploymentListView listView = new ApplicationDeploymentListView();
                ConverterUtil.convert(i, listView);
                Optional<Deployment> deploymentOptional = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), i.getName()));
                deploymentOptional.ifPresent(deployment -> {
                    DeploymentStatus status = deployment.getStatus();
                    Integer replicas = Optional.ofNullable(status.getReplicas()).orElse(0);
                    Integer readyReplicas = Optional.ofNullable(status.getReadyReplicas()).orElse(0);
                    listView.setReplicas(replicas);
                    listView.setReadyReplicas(readyReplicas);
                    if (replicas == 0 && readyReplicas == 0) {
                        listView.setRunStatus(RunStatus.Stopped.getValue());
                    } else {
                        if (replicas.equals(readyReplicas)) {
                            listView.setRunStatus(RunStatus.Running.getValue());
                        } else {
                            LocalDateTime time;
                            if (ObjectUtil.isNull(i.getModifyTime())) {
                                time = i.getCreateTime();
                            } else {
                                time = i.getModifyTime();
                            }
                            if(RunStatus.Preparing.getValue().equals(i.getRunStatus()) || RunStatus.Updating.getValue().equals(i.getRunStatus())) {
                                Duration duration = Duration.between(time, LocalDateTime.now());
                                long minutes = duration.toMinutes();
                                if (minutes > 5) {
                                    listView.setRunStatus(RunStatus.Error.getValue());
                                }
                            }
                        }
                    }
                    BizApplicationDeployment applicationDeployment = new BizApplicationDeployment();
                    applicationDeployment.setId(listView.getId());
                    applicationDeployment.setRunStatus(listView.getRunStatus());
                    bizApplicationDeploymentService.updateById(applicationDeployment);
                });
                return listView;
            }).collect(Collectors.toList());
            result.setRecords(mapList);
            result.setCurrent(queryPage.getCurrent());
            result.setSize(queryPage.getSize());
            result.setTotal(queryPage.getTotal());
            return result;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }

    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(String namespaceId, String id) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            BizApplicationDeployment deployment = bizApplicationDeploymentService.getById(id);
            bizApplicationDeploymentService.delete(id);
            bizServiceDiscoveryService.remove(Wrappers.<BizServiceDiscovery>lambdaQuery().eq(BizServiceDiscovery::getDeploymentId, id));
            deploymentService.delete(namespaceOptional.get().getName(), deployment.getName());
            networkService.deleteWithLabel(namespaceOptional.get().getName(), deployment.getName());
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }

    }

    @Override
    public ApplicationDeploymentDetailView get(String namespaceId, String id) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            ApplicationDeploymentDetailView applicationDeploymentDetailView = bizApplicationDeploymentService.get(id);
            Optional<Deployment> deploymentOptional = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), applicationDeploymentDetailView.getName()));
            if (deploymentOptional.isPresent()) {
                Deployment deployment = deploymentOptional.get();
                applicationDeploymentDetailView.setLabels(deployment.getMetadata().getLabels());
                applicationDeploymentDetailView.setReadyReplicas(deployment.getStatus().getReadyReplicas());
                applicationDeploymentDetailView.setAvailableReplicas(deployment.getStatus().getAvailableReplicas());
                applicationDeploymentDetailView.setUnavailableReplicas(deployment.getStatus().getUnavailableReplicas());
                applicationDeploymentDetailView.setUpdatedReplicas(deployment.getStatus().getUpdatedReplicas());
                applicationDeploymentDetailView.setReplicas(deployment.getStatus().getReplicas());
            }
            return applicationDeploymentDetailView;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }

    }

    @Override
    public void autoDeployment(String autoBuildId, String imageName, String tag) {
        try {
            LambdaQueryWrapper<BizApplicationDeployment> queryWrapper = Wrappers.<BizApplicationDeployment>lambdaQuery();
            queryWrapper.eq(BizApplicationDeployment::getAutoBuildId, autoBuildId);
            queryWrapper.eq(BizApplicationDeployment::getDelFlag, false);
            List<BizApplicationDeployment> applicationDeployments = bizApplicationDeploymentService.list(queryWrapper);
            for (BizApplicationDeployment applicationDeployment : applicationDeployments) {
                if (applicationDeployment.getImageName().equals(imageName)) {
                    BizNamespace namespace = bizNamespaceService.get(applicationDeployment.getNamespaceId());
                    ApplicationDeploymentDetailView detailView = bizApplicationDeploymentService.get(applicationDeployment.getId());
                    detailView.setImageName(imageName);
                    detailView.setImageTag(tag);

                    Deployment deployment = buildDeployment(namespace, detailView);
                    Optional<Deployment> optionalDeployment = Optional.ofNullable(deploymentService.get(namespace.getName(), detailView.getName()));
                    if (optionalDeployment.isPresent()) {
                        deploymentService.update(namespace.getName(), detailView.getName(), deployment);
                    } else {
                        deploymentService.create(namespace.getName(), deployment);
                    }

                    Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespace.getName(), detailView.getName()));
                    if (serviceOptional.isPresent()) {
                        if (StrUtil.isNotEmpty(detailView.getNetwork()) && !"none".equals(detailView.getNetwork())) {
                            networkService.update(namespace.getName(), detailView.getName(), buildService(detailView));
                        } else {
                            networkService.delete(namespace.getName(), detailView.getName());
                        }
                    } else {
                        if (StrUtil.isNotEmpty(detailView.getNetwork()) && !"none".equals(detailView.getNetwork())) {
                            networkService.create(namespace.getName(), buildService(detailView));
                        }
                    }


                    applicationDeployment.setImageName(imageName);
                    applicationDeployment.setImageTag(tag);
                    applicationDeployment.setRunStatus(RunStatus.Updating.getValue());
                    bizApplicationDeploymentService.updateForRobot(applicationDeployment);
                } else {
                    log.warn("触发自动更新，但镜像名称与部署名称不一致，忽略更新");
                }
            }
        } catch (Exception e) {
            log.error("自动部署失败");
        }
    }

    /**
     * 关闭应用
     *
     * @param namespaceId 命名空间ID
     * @param id          应用ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean shutdown(String namespaceId, String id) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            Optional<BizApplicationDeployment> applicationDeploymentOptional = Optional.ofNullable(bizApplicationDeploymentService.getById(id));
            if (applicationDeploymentOptional.isPresent()) {
                BizApplicationDeployment applicationDeployment = applicationDeploymentOptional.get();
                applicationDeployment.setRunStatus(RunStatus.Stopping.getValue());
                deploymentService.shutdown(namespaceOptional.get().getName(), applicationDeployment.getName());
                bizApplicationDeploymentService.update(applicationDeployment);
            } else {
                throw new BaseRuntimeException("应用不存在");
            }
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    /**
     * 启动应用
     *
     * @param namespaceId 命名空间ID
     * @param id          应用ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean start(String namespaceId, String id) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            Optional<BizApplicationDeployment> applicationDeploymentOptional = Optional.ofNullable(bizApplicationDeploymentService.getById(id));
            if (applicationDeploymentOptional.isPresent()) {
                BizApplicationDeployment applicationDeployment = applicationDeploymentOptional.get();
                applicationDeployment.setRunStatus(RunStatus.Preparing.getValue());
                deploymentService.start(namespaceOptional.get().getName(), applicationDeployment.getName(), applicationDeployment.getInstancesNumber());
                bizApplicationDeploymentService.update(applicationDeployment);
            } else {
                throw new BaseRuntimeException("应用不存在");
            }
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    /**
     * 应用扩容
     *
     * @param namespaceId 命名空间ID
     * @param id          应用ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean scale(String namespaceId, String id, Integer number) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            Optional<BizApplicationDeployment> applicationDeploymentOptional = Optional.ofNullable(bizApplicationDeploymentService.getById(id));
            if (applicationDeploymentOptional.isPresent()) {
                BizApplicationDeployment applicationDeployment = applicationDeploymentOptional.get();
                applicationDeployment.setRunStatus(RunStatus.Scaling.getValue());
                applicationDeployment.setInstancesNumber(number);
                deploymentService.scale(namespaceOptional.get().getName(), applicationDeployment.getName(), number);
                bizApplicationDeploymentService.update(applicationDeployment);
            } else {
                throw new BaseRuntimeException("应用不存在");
            }
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Deprecated
    public String yaml(String namespaceId, String id) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            BizNamespace namespace = namespaceOptional.get();
            ApplicationDeploymentDetailView detailView = bizApplicationDeploymentService.get(id);
            Deployment deployment = buildDeployment(namespace, detailView);
            io.fabric8.kubernetes.api.model.Service service = buildService(detailView);
            System.out.println(YamlUtil.objToYaml(deployment));
            System.out.println(YamlUtil.objToYaml(service));
            return "";
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    /**
     * 构建网络配置
     *
     * @return Service
     */
    private io.fabric8.kubernetes.api.model.Service buildService(ApplicationDeploymentDetailView deploymentDetail) {
        List<ApplicationDeploymentPortView> ports = JSONArray.parseArray(deploymentDetail.getPorts(), ApplicationDeploymentPortView.class);
        List<ServicePort> servicePorts = new ArrayList<>();
        for (ApplicationDeploymentPortView n : ports) {
            ServicePort servicePort = new ServicePort();
            servicePort.setName(deploymentDetail.getName() + "-http-" + n.getPort() + "-" + n.getTargetPort());
            servicePort.setAppProtocol(n.getProtocol());
            servicePort.setProtocol(n.getProtocol());
            servicePort.setPort(n.getPort());
            servicePort.setTargetPort(new IntOrString(n.getTargetPort()));
            servicePorts.add(servicePort);
        }

        ServiceBuilder builder = new ServiceBuilder()
                .withNewMetadata()
                .withName(deploymentDetail.getName())
                .withLabels(fetchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()), deploymentDetail.getImageTag()))
                .endMetadata();
        ServiceFluent.SpecNested<ServiceBuilder> spec = builder.withNewSpec();
        spec.addAllToPorts(servicePorts)
                .withSelector(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType())))
                .withType(deploymentDetail.getNetworkType());
        if (StrUtil.isNotEmpty(deploymentDetail.getExternalIp())) {
            spec.withExternalIPs(deploymentDetail.getExternalIp().split(","));
        } else {
            // 内部服务集群IP设置为空，生成Headless服务
            if ("internal".equals(deploymentDetail.getNetwork()) && "ClusterIP".equals(deploymentDetail.getNetworkType())) {
                spec.withClusterIP("None");
            }
        }
        spec.endSpec();
        return builder.build();
    }

    /**
     * 构建应用部署配置
     *
     * @param deploymentDetail 配置信息
     * @return Deployment
     */
    private Deployment buildDeployment(BizNamespace namespace, ApplicationDeploymentDetailView deploymentDetail) {
        Integer instancesNumber = deploymentDetail.getInstancesNumber();
        // 处于停止状态的应用，如果触发更新则设置实例数为1
        if (RunStatus.Stopping.getValue().equals(deploymentDetail.getRunStatus()) || RunStatus.Stopped.getValue().equals(deploymentDetail.getRunStatus())) {
            instancesNumber = 0;
        }

        return new DeploymentBuilder()
                .withNewMetadata()
                .withName(deploymentDetail.getName())
                .withLabels(fetchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()), deploymentDetail.getImageTag()))
                .endMetadata()
                .withNewSpec()
                .withReplicas(instancesNumber)
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(fetchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()), deploymentDetail.getImageTag()))
                .endMetadata()
                .withNewSpec()
                .withNodeName(deploymentDetail.getNode())
                .addNewContainer()
                .withName(deploymentDetail.getName())
                .withImage(deploymentDetail.getImageName() + ":" + deploymentDetail.getImageTag())
                .withEnv(fetchEnv(deploymentDetail.getEnvironmentVariable()))
                .withImagePullPolicy(deploymentDetail.getImagePullStrategy())
                .withVolumeMounts(fetchVolumeMount(deploymentDetail.getMounts()))
                .withReadinessProbe(deploymentDetail.getOpenReadinessProbe() ? fetchProbe(deploymentDetail.getReadinessProbe()) : null)
                .withLivenessProbe(deploymentDetail.getOpenLivenessProbe() ? fetchProbe(deploymentDetail.getLivenessProbe()) : null)
                .endContainer()
                .withVolumes(fetchVolume(deploymentDetail.getMounts()))
                .addNewImagePullSecret(namespace.getRegistrySecretName())
                .endSpec()
                .endTemplate()
                .withNewSelector()
                .withMatchLabels(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType())))
                .endSelector()
                .withStrategy(buildStrategy(deploymentDetail))
                .withRevisionHistoryLimit(deploymentDetail.getRevisionHistoryLimit())
                .endSpec()
                .build();
    }

    private List<Volume> fetchVolume(List<ApplicationDeploymentMountView> volumeViews) {
        List<Volume> volumes = new ArrayList<>();
        volumeViews.forEach(i -> {
            if (StrUtil.isNotEmpty(i.getVolumeType())) {
                VolumeBuilder volumeBuilder = new VolumeBuilder();
                volumeBuilder.withName(i.getMountName());
                if ("ConfigMap".equals(i.getVolumeType())) {
                    Optional<BizConfig> bizConfig = Optional.ofNullable(bizConfigService.get(i.getConfigId()));
                    if (bizConfig.isPresent()) {
                        BizConfig config = bizConfig.get();
                        volumeBuilder.withNewConfigMap()
                                .withName(config.getConfigName())
                                .endConfigMap();
                    } else {
                        throw new BaseRuntimeException("配置文件不存在");
                    }
                } else if ("EmptyDir".equals(i.getVolumeType())) {
                    volumeBuilder.withEmptyDir(new EmptyDirVolumeSource());
                } else if ("HostPath".equals(i.getVolumeType())) {
                    volumeBuilder.withHostPath(new HostPathVolumeSourceBuilder()
                            .withPath(i.getVolumePath())
                            .withType("DirectoryOrCreate")
                            .build());
                } else if ("PersistentVolumeClaim".equals(i.getVolumeType())) {
                    Optional<BizPersistentStorage> bizPersistentStorage = Optional.ofNullable(bizPersistentStorageService.get(i.getPersistentStorageId()));
                    if (bizPersistentStorage.isPresent()) {
                        BizPersistentStorage persistentStorage = bizPersistentStorage.get();
                        volumeBuilder.withPersistentVolumeClaim(new PersistentVolumeClaimVolumeSourceBuilder()
                                .withClaimName(persistentStorage.getName())
                                .withReadOnly(false)
                                .build());
                    } else {
                        throw new BaseRuntimeException("持久化存储配置不存在");
                    }
                }
                volumes.add(volumeBuilder.build());
            }
        });
        return volumes;
    }

    private List<VolumeMount> fetchVolumeMount(List<ApplicationDeploymentMountView> volumeViews) {
        List<VolumeMount> volumeMounts = new ArrayList<>();
        volumeViews.forEach(i -> {
            if (StrUtil.isNotEmpty(i.getVolumeType())) {
                VolumeMountBuilder volumeMountBuilder = new VolumeMountBuilder();
                volumeMountBuilder.withName(i.getMountName());
                if ("ConfigMap".equals(i.getVolumeType())) {
                    Optional<BizConfig> bizConfig = Optional.ofNullable(bizConfigService.get(i.getConfigId()));
                    if (bizConfig.isPresent()) {
                        BizConfig config = bizConfig.get();
                        volumeMountBuilder.withMountPath(i.getMountPath() + "/" + config.getFileName());
                        volumeMountBuilder.withSubPath(config.getFileName());
                    } else {
                        throw new BaseRuntimeException("配置文件不存在");
                    }
                } else {
                    volumeMountBuilder.withMountPath(i.getMountPath());
                }
                volumeMounts.add(volumeMountBuilder.build());
            }
        });
        return volumeMounts;
    }

    private DeploymentStrategy buildStrategy(ApplicationDeploymentDetailView deploymentDetail) {
        DeploymentStrategyBuilder deploymentStrategyBuilder = new DeploymentStrategyBuilder()
                .withType(deploymentDetail.getStrategyType());
        if ("RollingUpdate".equals(deploymentDetail.getStrategyType())) {
            deploymentStrategyBuilder
                    .withNewRollingUpdate()
                    .withMaxSurge(new IntOrString(deploymentDetail.getMaxSurge()))
                    .withMaxUnavailable(new IntOrString(deploymentDetail.getMaxUnavaible()))
                    .endRollingUpdate();
        }
        return deploymentStrategyBuilder
                .build();
    }

    private List<EnvVar> fetchEnv(String environmentVariable) {
        JSONArray env = JSONArray.parseArray(environmentVariable);
        List<EnvVar> list = new ArrayList<>();
        for (Object o : env) {
            JSONObject next = (JSONObject) o;
            if (!next.isEmpty()) {
                list.add(new EnvVarBuilder()
                        .withName(next.getString("key"))
                        .withValue(next.getString("value"))
                        .build());
            }
        }
        return list;
    }

    private Map<String, String> fetchLabel(String name, String envType, String version) {
        Map<String, String> label = new HashMap<>();
        label.put(KubernetesConstant.K8S_LABEL, name);
        label.put(KubernetesConstant.GLOBAL_LABEL, name);
        label.put(KubernetesConstant.ENVIRONMENT_LABEL, envType);
        label.put(KubernetesConstant.VERSION_LABEL, version);
        return label;
    }

    private Map<String, String> fetchMatchLabel(String name, String envType) {
        Map<String, String> label = new HashMap<>();
        label.put(KubernetesConstant.K8S_LABEL, name);
        label.put(KubernetesConstant.GLOBAL_LABEL, name);
        label.put(KubernetesConstant.ENVIRONMENT_LABEL, envType);
        return label;
    }

    private Probe fetchProbe(String probeSettings) {
        JSONObject probeObject = JSONObject.parseObject(probeSettings);
        ProbeBuilder probeBuilder = new ProbeBuilder();
        probeBuilder.withTimeoutSeconds(validProbeNumber(probeObject, "timeoutSeconds", 10))
                // 探针频率
                .withPeriodSeconds(validProbeNumber(probeObject, "periodSeconds", 10))
                // 延迟多少秒执行
                .withInitialDelaySeconds(validProbeNumber(probeObject, "initialDelaySeconds", 30))
                .withSuccessThreshold(validProbeNumber(probeObject, "successThreshold", 1))
                .withFailureThreshold(validProbeNumber(probeObject, "failureThreshold", 3));
        JSONArray array = probeObject.getJSONArray("probes");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (object.containsKey("strategyType")) {
                String strategyType = object.getString("strategyType");
                switch (strategyType) {
                    case "HTTPGet":
                        if (object.containsKey("path") && object.containsKey("port")) {
                            String path = object.getString("path");
                            Integer port = object.getInteger("port");
                            HTTPGetAction httpGetAction = new HTTPGetActionBuilder().withNewPath(path).withNewPort(port).withNewScheme("HTTP").build();
                            probeBuilder.withHttpGet(httpGetAction);
                        } else {
                            throw new BaseRuntimeException("HTTPGet探针配置错误");
                        }
                        break;
                    case "Exec":
                        if (object.containsKey("command")) {
                            String command = object.getString("command");
                            ExecAction execAction = new ExecActionBuilder().withCommand(command).build();
                            probeBuilder.withExec(execAction);
                        } else {
                            throw new BaseRuntimeException("Exec探针配置错误");
                        }
                        break;
                    case "TCPSocket":
                        if (object.containsKey("port")) {
                            Integer port = object.getInteger("port");
                            TCPSocketAction tcpSocketAction = new TCPSocketActionBuilder().withNewPort(port).build();
                            probeBuilder.withTcpSocket(tcpSocketAction);
                        } else {
                            throw new BaseRuntimeException("TCPSocket探针配置错误");
                        }
                        break;
                    default:
                        break;
                }
            } else {
                throw new BaseRuntimeException("探针配置错误");
            }
        }
        return probeBuilder.build();
    }

    private Integer validProbeNumber(JSONObject object, String key, int defaultValue) {
        int s;
        if (object.containsKey(key)) {
            Integer integer = object.getInteger(key);
            if (integer < 1) {
                s = defaultValue;
            } else {
                s = integer;
            }
        } else {
            s = defaultValue;
        }
        return s;
    }

}
