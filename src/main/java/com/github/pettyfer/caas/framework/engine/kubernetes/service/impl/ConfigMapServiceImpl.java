package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IConfigMapService;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class ConfigMapServiceImpl implements IConfigMapService {

    private final KubernetesClient kubernetesClient;

    public ConfigMapServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public ConfigMap create(String namespace, ConfigMap configMap) {
        return kubernetesClient.configMaps().inNamespace(namespace).create(configMap);
    }

    @Override
    public ConfigMap update(String namespace, String name, ConfigMap configMap) {
        return kubernetesClient.configMaps().inNamespace(namespace).withName(name).createOrReplace(configMap);
    }

    @Override
    public Boolean delete(String namespace, String name) {
        return kubernetesClient.configMaps().inNamespace(namespace).withName(name).delete();
    }

}
