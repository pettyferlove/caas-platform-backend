package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

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
