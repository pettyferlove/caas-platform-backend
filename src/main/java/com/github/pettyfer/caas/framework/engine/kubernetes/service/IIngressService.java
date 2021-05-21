package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.extensions.Ingress;

/**
 * @author Pettyfer
 */
public interface IIngressService {
    Ingress get(String namespace, String name);
}
