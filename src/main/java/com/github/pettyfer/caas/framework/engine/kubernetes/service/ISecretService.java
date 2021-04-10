package com.github.pettyfer.caas.framework.engine.kubernetes.service;

/**
 * @author Pettyfer
 */
public interface ISecretService {

    String createRegistrySecret(String namespace);

}
