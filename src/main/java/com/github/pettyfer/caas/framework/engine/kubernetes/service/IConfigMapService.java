package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.ConfigMap;

/**
 * @author Pettyfer
 */
public interface IConfigMapService {

    ConfigMap create(String namespace, ConfigMap configMap);

    ConfigMap update(String namespace, String name, ConfigMap configMap);

    Boolean delete(String namespace, String name);

}
