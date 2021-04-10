package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentNetwork;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentNetworkService;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentListView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentNetworkView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;
import com.github.pettyfer.caas.framework.core.service.IApplicationDeploymentCoreService;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.apps.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ApplicationDeploymentCoreServiceImpl implements IApplicationDeploymentCoreService {


    private final IBizApplicationDeploymentService bizApplicationDeploymentService;

    private final IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService;

    private final IBizNamespaceService bizNamespaceService;

    private final IDeploymentService deploymentService;

    private final INetworkService networkService;

    public ApplicationDeploymentCoreServiceImpl(IBizApplicationDeploymentService bizApplicationDeploymentService, IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService, IDeploymentService deploymentService, IBizNamespaceService bizNamespaceService, INetworkService networkService) {
        this.bizApplicationDeploymentService = bizApplicationDeploymentService;
        this.bizApplicationDeploymentNetworkService = bizApplicationDeploymentNetworkService;
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
                bizApplicationDeploymentNetworkService.batchInsert(deploymentId, deploymentDetail.getNetworks());

                // 组装部署配置
                Deployment deployment = buildDeployment(namespaceOptional.get(), deploymentDetail);
                deploymentService.create(namespaceOptional.get().getName(), deployment);

                // 组装网络配置
                if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                    networkService.createOrUpdate(namespaceOptional.get().getName(), buildService(deploymentDetail));
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
                    bizApplicationDeploymentNetworkService.remove(Wrappers.<BizApplicationDeploymentNetwork>lambdaQuery().eq(BizApplicationDeploymentNetwork::getDeploymentId, deploymentId));
                    bizApplicationDeploymentNetworkService.batchInsert(deploymentId, deploymentDetail.getNetworks());
                }
                Deployment deployment = buildDeployment(namespaceOptional.get(), deploymentDetail);
                deploymentService.update(namespaceOptional.get().getName(), bizApplicationDeployment.getName(), deployment);

                if (StrUtil.isNotEmpty(deploymentDetail.getNetwork()) && !"none".equals(deploymentDetail.getNetwork())) {
                    networkService.createOrUpdate(namespaceOptional.get().getName(), buildService(deploymentDetail));
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
            bizApplicationDeploymentNetworkService.remove(Wrappers.<BizApplicationDeploymentNetwork>lambdaQuery().eq(BizApplicationDeploymentNetwork::getDeploymentId, id));
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
            Deployment deployment = deploymentService.get(namespaceOptional.get().getName(), applicationDeploymentDetailView.getName());
            applicationDeploymentDetailView.setLabels(deployment.getMetadata().getLabels());
            applicationDeploymentDetailView.setReadyReplicas(deployment.getStatus().getReadyReplicas());
            applicationDeploymentDetailView.setAvailableReplicas(deployment.getStatus().getAvailableReplicas());
            applicationDeploymentDetailView.setUnavailableReplicas(deployment.getStatus().getUnavailableReplicas());
            applicationDeploymentDetailView.setUpdatedReplicas(deployment.getStatus().getUpdatedReplicas());
            applicationDeploymentDetailView.setReplicas(deployment.getStatus().getReplicas());
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
                    deploymentService.update(namespace.getName(), applicationDeployment.getName(), deployment);

                    if (StrUtil.isNotEmpty(detailView.getNetwork()) && !"none".equals(detailView.getNetwork())) {
                        networkService.createOrUpdate(namespace.getName(), buildService(detailView));
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
        List<ApplicationDeploymentNetworkView> networks = deploymentDetail.getNetworks();
        List<ServicePort> servicePorts = new ArrayList<>();
        for (ApplicationDeploymentNetworkView n : networks) {
            ServicePort servicePort = new ServicePort();
            servicePort.setName(deploymentDetail.getName() + "-http-" + RandomUtil.randomString(5));
            servicePort.setAppProtocol(n.getProtocol());
            servicePort.setProtocol(n.getProtocol());
            servicePort.setPort(n.getPort());
            servicePort.setTargetPort(new IntOrString(n.getTargetPort()));
            servicePorts.add(servicePort);
        }
        return new ServiceBuilder()
                .withNewMetadata()
                .withName(deploymentDetail.getName())
                .addToLabels(KubernetesConstant.GLOBAL_LABEL, deploymentDetail.getName())
                .addToLabels(KubernetesConstant.K8S_LABEL, deploymentDetail.getName())
                .endMetadata()
                .withNewSpec()
                .addAllToPorts(servicePorts)
                .addToSelector(KubernetesConstant.GLOBAL_LABEL, deploymentDetail.getName())
                .addToSelector(KubernetesConstant.K8S_LABEL, deploymentDetail.getName())
                .withType(deploymentDetail.getNetworkType())
                .withExternalIPs(deploymentDetail.getExternalIp().split(","))
                .endSpec()
                .build();
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
                .addToLabels(KubernetesConstant.GLOBAL_LABEL, deploymentDetail.getName())
                .addToLabels(KubernetesConstant.K8S_LABEL, deploymentDetail.getName())
                .addToLabels(KubernetesConstant.VERSION_LABEL, deploymentDetail.getImageTag())
                .endMetadata()
                .withNewSpec()
                .withReplicas(deploymentDetail.getInstancesNumber())
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels(KubernetesConstant.GLOBAL_LABEL, deploymentDetail.getName())
                .addToLabels(KubernetesConstant.K8S_LABEL, deploymentDetail.getName())
                .addToLabels(KubernetesConstant.VERSION_LABEL, deploymentDetail.getImageTag())
                .endMetadata()
                .withNewSpec()
                .withNodeName(deploymentDetail.getNode())
                .addNewContainer()
                .withName(deploymentDetail.getName())
                .withImage(deploymentDetail.getImageName() + ":" + deploymentDetail.getImageTag())
                .withImagePullPolicy(deploymentDetail.getImagePullStrategy())
                .endContainer()
                .addNewImagePullSecret(namespace.getRegistrySecretName())
                .endSpec()
                .endTemplate()
                .withNewSelector()
                .addToMatchLabels(KubernetesConstant.GLOBAL_LABEL, deploymentDetail.getName())
                .addToMatchLabels(KubernetesConstant.K8S_LABEL, deploymentDetail.getName())
                .endSelector()
                .withStrategy(buildStrategy(deploymentDetail))
                .withRevisionHistoryLimit(deploymentDetail.getRevisionHistoryLimit())
                .endSpec()
                .build();
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

}
