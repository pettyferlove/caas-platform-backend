package com.github.pettyfer.caas.framework.core.service;

/**
 * @author Pettyfer
 */
public interface ILogCoreService {

    String log(String namespaceId, String podName, String containerName);

}
