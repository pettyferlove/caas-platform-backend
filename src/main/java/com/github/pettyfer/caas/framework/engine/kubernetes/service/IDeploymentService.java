package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.apps.Deployment;

/**
 * @author Petty
 */
public interface IDeploymentService {

    /**
     * 创建应用
     * @param namespace 命名空间
     * @param deployment 部署信息
     * @return 返回信息
     */
    Deployment create(String namespace, Deployment deployment);

    /**
     * 更新应用
     * @param namespace 命名空间
     * @param name 名称
     * @param deployment 部署信息
     * @return 返回信息
     */
    Deployment update(String namespace, String name, Deployment deployment);

    /**
     * 获取应用信息
     * @param namespace 命名空间
     * @param name 名称
     * @return 返回信息
     */
    Deployment get(String namespace, String name);

    /**
     * 删除应用
     * @param namespace 命名空间
     * @param name 名称
     */
    void delete(String namespace, String name);

    /**
     * 停止应用
     * @param namespace 命名空间ID
     * @param name 应用名
     */
    void shutdown(String namespace, String name);

    /**
     * 开启应用
     * @param namespace 命名空间ID
     * @param name 应用名
     * @param instancesNumber 实例数量
     */
    void start(String namespace, String name, Integer instancesNumber);

    /**
     * 应用扩容
     * @param namespace 命名空间ID
     * @param name 应用名
     * @param instancesNumber 实例数量
     */
    void scale(String namespace, String name, Integer instancesNumber);
}
