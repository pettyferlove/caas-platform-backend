package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.constants.NetworkType;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@org.springframework.stereotype.Service
public class NetworkServiceImpl implements INetworkService {

    private final KubernetesClient kubernetesClient;

    public NetworkServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Service createOrUpdate(String namespace, Service service) {
        return kubernetesClient.services().inNamespace(namespace).createOrReplace(service);
    }

    @Override
    public void update(String namespace, String name, Service service) {
        Optional<Service> optionalService = Optional.ofNullable(kubernetesClient.services().inNamespace(namespace).withName(name).get());
        if (optionalService.isPresent()) {
            // 如果是集群IP则更新时删除原有的服务
            if (NetworkType.ClusterIP.getValue().equals(service.getSpec().getType())) {
                kubernetesClient.services().inNamespace(namespace).withName(name).delete();
            }
            if (NetworkType.NodePort.getValue().equals(service.getSpec().getType())) {
                List<ServicePort> oldPorts = optionalService.get().getSpec().getPorts();
                List<ServicePort> ports = service.getSpec().getPorts();
                for (ServicePort port : ports) {
                    Optional<ServicePort> first = oldPorts.stream().filter(i -> i.getName().equals(port.getName())).findFirst();
                    first.ifPresent(servicePort -> port.setNodePort(servicePort.getNodePort()));
                }
            }
            if(NetworkType.ClusterIP.getValue().equals(optionalService.get().getSpec().getType()) && "None".equals(optionalService.get().getSpec().getClusterIP())) {
                kubernetesClient.services().inNamespace(namespace).withName(name).delete();
            }
        }
        kubernetesClient.services().inNamespace(namespace).withName(name).createOrReplace(service);
    }

    @Override
    public void create(String namespace, Service service) {
        kubernetesClient.services().inNamespace(namespace).create(service);
    }

    @Override
    public void deleteWithLabel(String namespace, String name) {
        kubernetesClient.services().inNamespace(namespace).withLabel(KubernetesConstant.GLOBAL_LABEL, name).delete();
    }

    @Override
    public void delete(String namespace, String name) {
        kubernetesClient.services().inNamespace(namespace).withName(name).delete();
    }

    @Override
    public Service get(String namespace, String name) {
        return kubernetesClient.services().inNamespace(namespace).withName(name).get();
    }
}
