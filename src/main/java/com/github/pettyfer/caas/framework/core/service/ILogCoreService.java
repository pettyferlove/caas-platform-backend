package com.github.pettyfer.caas.framework.core.service;

/**
 * @author Pettyfer
 */
public interface ILogCoreService {

    String buildLog(String namespaceId, String podName, String containerName);

    String applicationLog(String namespaceId, String podName);

}
