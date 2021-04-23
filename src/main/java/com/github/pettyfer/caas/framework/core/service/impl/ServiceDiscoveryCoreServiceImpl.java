package com.github.pettyfer.caas.framework.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.entity.BizServiceDiscovery;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.biz.service.IBizServiceDiscoveryService;
import com.github.pettyfer.caas.framework.core.model.ServiceDiscoveryListView;
import com.github.pettyfer.caas.framework.core.service.IServiceDiscoveryCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NodeDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INodeService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import io.fabric8.kubernetes.api.model.ServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ServiceDiscoveryCoreServiceImpl implements IServiceDiscoveryCoreService {

    private final IBizServiceDiscoveryService bizServiceDiscoveryService;

    private final IBizNamespaceService bizNamespaceService;

    private final INetworkService networkService;

    private final INodeService nodeService;

    public ServiceDiscoveryCoreServiceImpl(IBizServiceDiscoveryService bizServiceDiscoveryService, IBizNamespaceService bizNamespaceService, INetworkService networkService, INodeService nodeService) {
        this.bizServiceDiscoveryService = bizServiceDiscoveryService;
        this.bizNamespaceService = bizNamespaceService;
        this.networkService = networkService;
        this.nodeService = nodeService;
    }

    @Override
    public IPage<ServiceDiscoveryListView> page(String namespaceId, ServiceDiscoveryListView serviceDiscoveryListView, Page<ServiceDiscoveryListView> page) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            BizServiceDiscovery bizServiceDiscovery = new BizServiceDiscovery();
            Page<BizServiceDiscovery> bizServiceDiscoveryPage = new Page<>();
            ConverterUtil.convert(serviceDiscoveryListView, bizServiceDiscovery);
            ConverterUtil.convert(page, bizServiceDiscoveryPage);
            IPage<ServiceDiscoveryListView> result = new Page<>();
            IPage<BizServiceDiscovery> queryPage = bizServiceDiscoveryService.page(namespaceId, bizServiceDiscovery, bizServiceDiscoveryPage);
            List<BizServiceDiscovery> records = queryPage.getRecords();
            List<ServiceDiscoveryListView> mapList = records.stream().map(i -> {
                ServiceDiscoveryListView discoveryListView = new ServiceDiscoveryListView();
                ConverterUtil.convert(i, discoveryListView);
                Optional<io.fabric8.kubernetes.api.model.Service> serviceOptional = Optional.ofNullable(networkService.get(namespaceOptional.get().getName(), i.getName()));
                if (serviceOptional.isPresent()) {
                    io.fabric8.kubernetes.api.model.Service service = serviceOptional.get();
                    discoveryListView.setClusterIp(service.getSpec().getClusterIP());
                    String name = service.getMetadata().getName();
                    String namespace = service.getMetadata().getNamespace();
                    List<ServicePort> ports = service.getSpec().getPorts();
                    List<String> internalEndpoints = new LinkedList<>();
                    List<String> externalEndpoints = new LinkedList<>();
                    for (ServicePort port : ports) {
                        if ("NodePort".equals(service.getSpec().getType())) {
                            List<NodeDetailView> list = nodeService.list();
                            for (NodeDetailView node : list) {
                                externalEndpoints.add(node.getIp() + ":" + port.getNodePort());
                            }
                        }
                        internalEndpoints.add(name + "." + namespace + ":" + port.getPort() + " " + port.getProtocol());
                    }
                    discoveryListView.setInternalEndpoints(String.join(",", internalEndpoints));
                    List<String> externalIPs = service.getSpec().getExternalIPs();
                    for (String externalIP : externalIPs) {
                        for (ServicePort port : ports) {
                            externalEndpoints.add(externalIP + ":" + port.getPort());
                        }
                    }
                    discoveryListView.setExternalEndpoints(String.join(",", externalEndpoints));
                }
                return discoveryListView;
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

}
