package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IStatefulSetService;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class StatefulSetServiceImpl implements IStatefulSetService {

    private final KubernetesClient kubernetesClient;

    public StatefulSetServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public StatefulSet get(String namespace, String name) {
        return kubernetesClient.apps().statefulSets().inNamespace(namespace).withName(name).get();
    }
}
