package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.core.service.INamespaceCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INamespaceService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.ISecretService;
import com.github.pettyfer.caas.utils.SecurityUtil;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class NamespaceCoreServiceImpl implements INamespaceCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final INamespaceService namespaceService;

    private final ISecretService secretService;

    public NamespaceCoreServiceImpl(IBizNamespaceService bizNamespaceService, INamespaceService namespaceService, ISecretService secretService) {
        this.bizNamespaceService = bizNamespaceService;
        this.namespaceService = namespaceService;
        this.secretService = secretService;
    }

    @Override
    public List<BizNamespace> listAll() {
        LambdaQueryWrapper<BizNamespace> queryWrapper = Wrappers.<BizNamespace>lambdaQuery();
        queryWrapper.eq(BizNamespace::getCreator, SecurityUtil.getUser().getId());
        queryWrapper.eq(BizNamespace::getDelFlag, 0);
        return bizNamespaceService.list(queryWrapper);
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
        try {
            String registrySecret = secretService.createRegistrySecret(namespace.getName());
            LambdaUpdateWrapper<BizNamespace> updateWrapper = Wrappers.<BizNamespace>lambdaUpdate();
            updateWrapper.set(BizNamespace::getRegistrySecretName, registrySecret);
            updateWrapper.eq(BizNamespace::getId, id);
            updateWrapper.eq(BizNamespace::getDelFlag, false);
            bizNamespaceService.update(updateWrapper);
        } catch (Exception e) {
            // 如果发生异常则删除命名空间
            namespaceService.delete(namespace.getName());
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean delete(String id) {
        BizNamespace bizNamespace = bizNamespaceService.get(id);
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
        if (ObjectUtil.isNull(namespace.getIstio())) {
            namespace.setIstio(true);
        }
        label.put("istio-injection", namespace.getIstio() ? "enable" : "disable");
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
