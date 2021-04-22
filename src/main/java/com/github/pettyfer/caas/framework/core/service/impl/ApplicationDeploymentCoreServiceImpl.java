package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ApplicationDeploymentCoreServiceImpl(IBizApplicationDeploymentService bizApplicationDeploymentService, IBizServiceDiscoveryService bizServiceDiscoveryService, IBizApplicationDeploymentMountService bizApplicationDeploymentVolumeService, IBizConfigService bizConfigService, IBizPersistentStorageService bizPersistentStorageService, IDeploymentService deploymentService, IBizNamespaceService bizNamespaceService, INetworkService networkService) {
        this.bizApplicationDeploymentService = bizApplicationDeploymentService;
        this.bizServiceDiscoveryService = bizServiceDiscoveryService;
        this.bizApplicationDeploymentVolumeService = bizApplicationDeploymentVolumeService;
        this.bizConfigService = bizConfigService;
        this.bizPersistentStorageService = bizPersistentStorageService;
        this.deploymentService = deploymentService;
        this.bizNamespaceService = bizNamespaceService;
        this.networkService = networkService;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(String namespaceId, ApplicationDeploymentDetailView deploymentDetail) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            try {
                BizApplicationDeployment bizApplicationDeployment = new BizApplicationDeployment();
                ConverterUtil.convert(deploymentDetail, bizApplicationDeployment);
                String deploymentId = bizApplicationDeploymentService.create(namespaceId, bizApplicationDeployment);


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
                if(optionalDeployment.isPresent()){
                    deploymentService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), deployment);
                } else {
                    deploymentService.create(namespaceOptional.get().getName(), deployment);
                }
                if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                    Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                    if(serviceOptional.isPresent()) {
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
                Boolean update = bizApplicationDeploymentService.update(namespaceId, bizApplicationDeployment);
                if (update) {
                    bizServiceDiscoveryService.remove(Wrappers.<BizServiceDiscovery>lambdaQuery().eq(BizServiceDiscovery::getDeploymentId, deploymentId));
                    BizServiceDiscovery bizServiceDiscovery = new BizServiceDiscovery();
                    ConverterUtil.convert(deploymentDetail, bizServiceDiscovery);
                    bizServiceDiscovery.setId(null);
                    bizServiceDiscovery.setNamespaceId(namespaceId);
                    bizServiceDiscovery.setDeploymentId(deploymentId);
                    bizServiceDiscovery.setName(deploymentDetail.getName());
                    bizServiceDiscovery.setMatchLabel(JSON.toJSONString(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()))));
                    bizServiceDiscoveryService.create(bizServiceDiscovery);

                    bizApplicationDeploymentVolumeService.remove(Wrappers.<BizApplicationDeploymentMount>lambdaQuery().eq(BizApplicationDeploymentMount::getDeploymentId, deploymentId));
                    bizApplicationDeploymentVolumeService.batchInsert(deploymentId, deploymentDetail.getMounts());
                }

                Deployment deployment = buildDeployment(namespaceOptional.get(), deploymentDetail);
                Optional<Deployment> optionalDeployment = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                if(optionalDeployment.isPresent()){
                    deploymentService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), deployment);
                } else {
                    deploymentService.create(namespaceOptional.get().getName(), deployment);
                }
                if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                    Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespaceOptional.get().getName(), deploymentDetail.getName()));
                    if(serviceOptional.isPresent()) {
                        networkService.update(namespaceOptional.get().getName(), deploymentDetail.getName(), buildService(deploymentDetail));
                    } else {
                        networkService.create(namespaceOptional.get().getName(), buildService(deploymentDetail));
                    }
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
                ApplicationDeploymentListView deploymentList = new ApplicationDeploymentListView();
                ConverterUtil.convert(i, deploymentList);
                Optional<Deployment> deploymentOptional = Optional.ofNullable(deploymentService.get(namespaceOptional.get().getName(), i.getName()));
                deploymentOptional.ifPresent(deployment -> {
                    DeploymentStatus status = deployment.getStatus();
                    deploymentList.setReplicas(Optional.ofNullable(status.getReplicas()).orElse(0));
                    deploymentList.setReadyReplicas(Optional.ofNullable(status.getReadyReplicas()).orElse(0));
                });
                return deploymentList;
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
                    if(optionalDeployment.isPresent()){
                        deploymentService.update(namespace.getName(), detailView.getName(), deployment);
                    } else {
                        deploymentService.create(namespace.getName(), deployment);
                    }
                    if (StrUtil.isNotEmpty(detailView.getNetwork()) && !"none".equals(detailView.getNetwork())) {
                        Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespace.getName(), detailView.getName()));
                        if(serviceOptional.isPresent()) {
                            networkService.update(namespace.getName(), detailView.getName(), buildService(detailView));
                        } else {
                            networkService.create(namespace.getName(), buildService(detailView));
                        }
                    }

                    applicationDeployment.setImageName(imageName);
                    applicationDeployment.setImageTag(tag);
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
     * 构建网络配置
     *
     * @return Service
     */
    private io.fabric8.kubernetes.api.model.Service buildService(ApplicationDeploymentDetailView deploymentDetail) {
        List<ApplicationDeploymentPortView> ports = JSONArray.parseArray(deploymentDetail.getPorts(), ApplicationDeploymentPortView.class);
        List<ServicePort> servicePorts = new ArrayList<>();
        for (ApplicationDeploymentPortView n : ports) {
            ServicePort servicePort = new ServicePort();
            servicePort.setName(deploymentDetail.getName() + "-http-" + n.getPort() +"-" + n.getTargetPort());
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
        if (StrUtil.isNotEmpty(deploymentDetail.getExternalIp())) {
            builder.withNewSpec()
                    .addAllToPorts(servicePorts)
                    .withSelector(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType())))
                    .withType(deploymentDetail.getNetworkType())
                    .withExternalIPs(deploymentDetail.getExternalIp().split(","))
                    .endSpec();
        } else {
            builder.withNewSpec()
                    .addAllToPorts(servicePorts)
                    .withSelector(fetchMatchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType())))
                    .withType(deploymentDetail.getNetworkType())
                    .endSpec();
        }
        return builder.build();
    }

    /**
     * 构建应用部署配置
     *
     * @param deploymentDetail 配置信息
     * @return Deployment
     */
    private Deployment buildDeployment(BizNamespace namespace, ApplicationDeploymentDetailView deploymentDetail) {
        return new DeploymentBuilder()
                .withNewMetadata()
                .withName(deploymentDetail.getName())
                .withLabels(fetchLabel(deploymentDetail.getName(), EnvConstant.transform(deploymentDetail.getEnvType()), deploymentDetail.getImageTag()))
                .endMetadata()
                .withNewSpec()
                .withReplicas(deploymentDetail.getInstancesNumber())
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
                    if(bizPersistentStorage.isPresent()) {
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

}
