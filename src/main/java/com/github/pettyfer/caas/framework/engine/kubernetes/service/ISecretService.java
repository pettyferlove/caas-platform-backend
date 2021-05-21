package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.Secret;

/**
 * @author Pettyfer
 */
public interface ISecretService {

    String createRegistrySecret(String namespace);

    Secret get(String namespace, String name);
}
