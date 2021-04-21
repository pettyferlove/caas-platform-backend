package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.core.model.ServiceDiscoveryListView;
import com.github.pettyfer.caas.framework.core.service.IServiceDiscoveryCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/service-discovery")
public class ServiceDiscoveryApi {

    private final IServiceDiscoveryCoreService serviceDiscoveryCoreService;

    public ServiceDiscoveryApi(IServiceDiscoveryCoreService serviceDiscoveryCoreService) {
        this.serviceDiscoveryCoreService = serviceDiscoveryCoreService;
    }

    @GetMapping("page/{namespaceId}")
    public R<IPage<ServiceDiscoveryListView>> page(@PathVariable String namespaceId, ServiceDiscoveryListView serviceDiscoveryListView, Page<ServiceDiscoveryListView> page) {
        return new R<>(serviceDiscoveryCoreService.page(namespaceId, serviceDiscoveryListView, page));
    }

}
