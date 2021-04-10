package com.github.pettyfer.caas.framework.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentNetwork;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentNetworkService;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.core.service.INamespaceCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INamespaceService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.ISecretService;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class NamespaceCoreServiceImpl implements INamespaceCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IBizApplicationDeploymentService bizApplicationDeploymentService;

    private final IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService;

    private final INamespaceService namespaceService;

    private final ISecretService secretService;

    public NamespaceCoreServiceImpl(IBizNamespaceService bizNamespaceService, IBizApplicationDeploymentService bizApplicationDeploymentService, IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService, INamespaceService namespaceService, ISecretService secretService) {
        this.bizNamespaceService = bizNamespaceService;
        this.bizApplicationDeploymentService = bizApplicationDeploymentService;
        this.bizApplicationDeploymentNetworkService = bizApplicationDeploymentNetworkService;
        this.namespaceService = namespaceService;
        this.secretService = secretService;
    }

    @Override
    public List<BizNamespace> listAll() {
        return bizNamespaceService.list();
    }

    @Override
    public IPage<BizNamespace> page(BizNamespace bizNamespace, Page<BizNamespace> page) {
        return bizNamespaceService.page(bizNamespace, page);
    }

    @Override
    public NamespaceDetailView detail(String id) {
        Optional<BizNamespace> optionalBizNamespace = Optional.ofNullable(bizNamespaceService.get(id));
        if (optionalBizNamespace.isPresent()) {
            BizNamespace bizNamespace = optionalBizNamespace.get();
            return namespaceService.get(bizNamespace.getName());
        } else {
            throw new RuntimeException("命名空间不存在");
        }

    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean create(BizNamespace namespace) {
        String id = bizNamespaceService.create(namespace);
        namespaceService.create(buildNamespace(namespace));
        String registrySecret = secretService.createRegistrySecret(namespace.getName());
        LambdaUpdateWrapper<BizNamespace> updateWrapper = Wrappers.<BizNamespace>lambdaUpdate();
        updateWrapper.set(BizNamespace::getRegistrySecretName, registrySecret);
        updateWrapper.eq(BizNamespace::getId, id);
        updateWrapper.eq(BizNamespace::getDelFlag, false);
        bizNamespaceService.update(updateWrapper);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean delete(String id) {
        BizNamespace bizNamespace = bizNamespaceService.get(id);
        List<BizApplicationDeployment> bizApplicationDeployments = bizApplicationDeploymentService.list(Wrappers.<BizApplicationDeployment>lambdaQuery().eq(BizApplicationDeployment::getNamespaceId, id).eq(BizApplicationDeployment::getDelFlag, false));
        List<String> bizApplicationDeploymentIds = bizApplicationDeployments.stream().map(BizApplicationDeployment::getId).collect(Collectors.toList());
        if (!bizApplicationDeploymentIds.isEmpty()) {
            bizApplicationDeploymentService.remove(Wrappers.<BizApplicationDeployment>lambdaQuery().in(BizApplicationDeployment::getId, bizApplicationDeploymentIds));
            bizApplicationDeploymentNetworkService.remove(Wrappers.<BizApplicationDeploymentNetwork>lambdaQuery().in(BizApplicationDeploymentNetwork::getDeploymentId, bizApplicationDeploymentIds));
        }
        bizNamespaceService.delete(id);
        namespaceService.delete(bizNamespace.getName());
        return true;
    }

    @Override
    public BizNamespace get(String id) {
        return bizNamespaceService.get(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean update(BizNamespace namespace) {
        bizNamespaceService.update(namespace);
        namespaceService.update(namespace.getName(), buildNamespace(namespace));
        return true;
    }

    private Namespace buildNamespace(BizNamespace namespace) {
        Map<String, String> label = new HashMap<>();
        label.put("istio-injection", namespace.getIstio() ? "enable" : "disable");
        label.put(KubernetesConstant.ENVIRONMENT_LABEL, EnvConstant.transform(namespace.getEnvType()));
        return new NamespaceBuilder()
                .withApiVersion("v1")
                .withKind("Namespace")
                .withNewMetadata()
                .withName(namespace.getName())
                .withLabels(label)
                .endMetadata()
                .build();
    }

}
