package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.core.model.ServiceDiscoveryListView;

/**
 * @author Pettyfer
 */
public interface IServiceDiscoveryCoreService {

    IPage<ServiceDiscoveryListView> page(String namespaceId, ServiceDiscoveryListView serviceDiscoveryListView, Page<ServiceDiscoveryListView> page);

}
