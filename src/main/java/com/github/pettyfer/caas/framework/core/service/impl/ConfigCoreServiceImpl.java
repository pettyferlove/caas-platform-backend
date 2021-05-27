package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizConfigService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import com.github.pettyfer.caas.framework.core.model.ConfigSelectView;
import com.github.pettyfer.caas.framework.core.service.IConfigCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IConfigMapService;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ConfigCoreServiceImpl implements IConfigCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IBizConfigService bizConfigService;

    private final IConfigMapService configMapService;

    public ConfigCoreServiceImpl(IBizNamespaceService bizNamespaceService, IBizConfigService bizConfigService, IConfigMapService configMapService) {
        this.bizNamespaceService = bizNamespaceService;
        this.bizConfigService = bizConfigService;
        this.configMapService = configMapService;
    }

    @Override
    public IPage<ConfigListView> page(BizConfig config, Page<BizConfig> page) {
        return bizConfigService.page(config, page);
    }

    @Override
    public BizConfig get(String id) {
        return bizConfigService.get(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean update(BizConfig bizConfig) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizConfig.getNamespaceId()));
        if (bizNamespaceOptional.isPresent()) {
            bizConfigService.update(bizConfig);
            configMapService.update(bizNamespaceOptional.get().getName(), bizConfig.getConfigName(), buildConfigMap(bizNamespaceOptional.get(), bizConfig));
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(BizConfig bizConfig) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizConfig.getNamespaceId()));
        if (bizNamespaceOptional.isPresent()) {
            String id = bizConfigService.create(bizConfig);
            configMapService.create(bizNamespaceOptional.get().getName(), buildConfigMap(bizNamespaceOptional.get(), bizConfig));
            return id;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean delete(String id) {
        Optional<BizConfig> optionalBizConfig = Optional.ofNullable(bizConfigService.get(id));
        if (optionalBizConfig.isPresent()) {
            Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(optionalBizConfig.get().getNamespaceId()));
            if (bizNamespaceOptional.isPresent()) {
                configMapService.delete(bizNamespaceOptional.get().getName(), optionalBizConfig.get().getConfigName());
            } else {
                throw new BaseRuntimeException("命名空间不存在");
            }
        } else {
            throw new BaseRuntimeException("配置不存在");
        }
        return bizConfigService.delete(id);
    }

    @Override
    public List<ConfigSelectView> configSelect(String namespaceId, Integer envType) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (bizNamespaceOptional.isPresent()) {
            LambdaQueryWrapper<BizConfig> queryWrapper = Wrappers.<BizConfig>lambdaQuery();
            queryWrapper.eq(BizConfig::getDelFlag, 0);
            queryWrapper.eq(ObjectUtil.isNotNull(envType),BizConfig::getEnvType, envType);
            List<BizConfig> list = bizConfigService.list(queryWrapper);
            return Optional.ofNullable(ConverterUtil.convertList(BizConfig.class, ConfigSelectView.class, list)).orElseGet(ArrayList::new);
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }


    private ConfigMap buildConfigMap(BizNamespace namespace, BizConfig config) {
        return new ConfigMapBuilder()
                .withNewMetadata()
                .withName(config.getConfigName())
                .addToLabels(KubernetesConstant.GLOBAL_LABEL, config.getConfigName())
                .addToLabels(KubernetesConstant.K8S_LABEL, config.getConfigName())
                .addToLabels(KubernetesConstant.ENVIRONMENT_LABEL, EnvConstant.transform(config.getEnvType()))
                .endMetadata()
                .addToData(config.getFileName(), config.getContent())
                .build();

    }

}
