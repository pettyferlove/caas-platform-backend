package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Petty
 */
@Slf4j
@Service
public class DeploymentServiceImpl implements IDeploymentService {

    private final KubernetesClient kubernetesClient;

    public DeploymentServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Deployment create(String namespace, Deployment deployment) {
        return kubernetesClient.apps().deployments().inNamespace(namespace).create(deployment);
    }

    @Override
    public Deployment update(String namespace, String name, Deployment deployment) {
        return kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).createOrReplace(deployment);
    }

    @Override
    public Deployment get(String namespace, String name) {
        return kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).get();
    }

    @Override
    public void delete(String namespace, String name) {
        kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).delete();
    }

    /**
     * 停止应用
     *
     * @param namespace 命名空间ID
     * @param name      应用名
     */
    @Override
    public void shutdown(String namespace, String name) {
        kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).scale(0);
    }

    /**
     * 开启应用
     *
     * @param namespace       命名空间ID
     * @param name            应用名
     * @param instancesNumber 实例数量
     */
    @Override
    public void start(String namespace, String name, Integer instancesNumber) {
        kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).scale(instancesNumber);
    }

    /**
     * 应用扩容
     *
     * @param namespace       命名空间ID
     * @param name            应用名
     * @param instancesNumber 实例数量
     */
    @Override
    public void scale(String namespace, String name, Integer instancesNumber) {
        kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).scale(instancesNumber);
    }
}
