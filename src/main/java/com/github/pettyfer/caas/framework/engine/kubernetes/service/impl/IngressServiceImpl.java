package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IIngressService;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class IngressServiceImpl implements IIngressService {

    private final KubernetesClient kubernetesClient;

    public IngressServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Ingress get(String namespace, String name) {
        return kubernetesClient.extensions().ingresses().inNamespace(namespace).withName(name).get();
    }
}
