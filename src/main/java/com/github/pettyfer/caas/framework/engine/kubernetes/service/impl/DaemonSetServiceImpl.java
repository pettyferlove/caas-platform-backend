package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDaemonSetService;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class DaemonSetServiceImpl implements IDaemonSetService {

    private final KubernetesClient kubernetesClient;

    public DaemonSetServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public DaemonSet get(String namespace, String name) {
        return kubernetesClient.apps().daemonSets().inNamespace(namespace).withName(name).get();
    }
}
