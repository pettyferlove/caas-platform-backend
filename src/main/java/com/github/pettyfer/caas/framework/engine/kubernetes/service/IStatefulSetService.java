package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;

/**
 * @author Pettyfer
 */
public interface IStatefulSetService {
    StatefulSet get(String namespace, String name);
}
