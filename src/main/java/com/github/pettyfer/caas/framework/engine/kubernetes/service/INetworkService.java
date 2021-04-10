package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;

/**
 * @author Pettyfer
 */
public interface INetworkService {

    Service createOrUpdate(String namespace, Service service);

    void deleteWithLabel(String namespace, String deploymentName);

}
