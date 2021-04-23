package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.Service;

/**
 * @author Pettyfer
 */
public interface INetworkService {

    /**
     * 创建或更新网络配置
     *
     * @param namespace 命名空间
     * @param service   网络设置具体数据
     * @return Service
     */
    Service createOrUpdate(String namespace, Service service);

    void update(String namespace, String name, Service service);

    void create(String namespace, Service service);

    /**
     * 根据标签批量删除网络配置
     *
     * @param namespace 命名空间
     * @param name      部署名称
     */
    void deleteWithLabel(String namespace, String name);

    /**
     * 根据名称进行删除
     *
     * @param namespace 命名空间
     * @param name      部署名称
     */
    void delete(String namespace, String name);

    Service get(String namespace, String name);

}
