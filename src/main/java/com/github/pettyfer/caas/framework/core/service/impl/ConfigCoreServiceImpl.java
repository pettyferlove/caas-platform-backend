package com.github.pettyfer.caas.framework.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizConfigService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import com.github.pettyfer.caas.framework.core.service.IConfigCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ConfigCoreServiceImpl implements IConfigCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IBizConfigService bizConfigService;

    public ConfigCoreServiceImpl(IBizNamespaceService bizNamespaceService, IBizConfigService bizConfigService) {
        this.bizNamespaceService = bizNamespaceService;
        this.bizConfigService = bizConfigService;
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
    public Boolean update(BizConfig bizConfig) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizConfig.getNamespaceId()));
        if (bizNamespaceOptional.isPresent()) {
            return bizConfigService.update(bizConfig);
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public String create(BizConfig bizConfig) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(bizConfig.getNamespaceId()));
        if (bizNamespaceOptional.isPresent()) {
            return bizConfigService.create(bizConfig);
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public Boolean delete(String id) {
        return bizConfigService.delete(id);
    }
}
