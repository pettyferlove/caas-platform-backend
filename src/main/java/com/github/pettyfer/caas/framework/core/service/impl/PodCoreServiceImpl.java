package com.github.pettyfer.caas.framework.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.core.service.IPodCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.PodPageView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IPodService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class PodCoreServiceImpl implements IPodCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IBizApplicationDeploymentService bizApplicationDeploymentService;

    private final IPodService podService;

    public PodCoreServiceImpl(IBizNamespaceService bizNamespaceService, IBizApplicationDeploymentService bizApplicationDeploymentService, IPodService podService) {
        this.bizNamespaceService = bizNamespaceService;
        this.bizApplicationDeploymentService = bizApplicationDeploymentService;
        this.podService = podService;
    }


    @Override
    public Boolean delete(String namespaceId, String deploymentId) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            Optional<BizApplicationDeployment> optionalDeployment = Optional.ofNullable(bizApplicationDeploymentService.getById(deploymentId));
            if (optionalDeployment.isPresent()) {
                podService.delete(namespaceOptional.get().getName(), optionalDeployment.get().getName());
                return true;
            } else {
                throw new BaseRuntimeException("应用不存在");
            }
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public Page<PodPageView> page(String namespaceId, String deploymentId, ListQueryParams params) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            Optional<BizApplicationDeployment> optionalDeployment = Optional.ofNullable(bizApplicationDeploymentService.getById(deploymentId));
            if (optionalDeployment.isPresent()) {
                return podService.page(namespaceOptional.get().getName(), optionalDeployment.get().getName(), params);
            } else {
                throw new BaseRuntimeException("应用不存在");
            }
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

}
