package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.apps.DaemonSet;

/**
 * @author Pettyfer
 */
public interface IDaemonSetService {
    DaemonSet get(String namespace, String name);
}
