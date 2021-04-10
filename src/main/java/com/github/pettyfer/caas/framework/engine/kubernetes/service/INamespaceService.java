package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespacePageView;
import com.github.pettyfer.caas.global.model.Page;
import io.fabric8.kubernetes.api.model.Namespace;

import java.util.List;

/**
 * @author Petty
 */
public interface INamespaceService {

    /**
     * 列出所有的Namespace
     * @return Page
     */
    List<NamespacePageView> listAll();

    /**
     * 列出当前用户（Kubernetes用户）下的所有的Namespace
     * @param params ListQueryParams
     * @return Page
     */
    Page<NamespacePageView> page(ListQueryParams params);

    /**
     * 创建Namespace
     * @param namespace 名称
     * @return Boolean
     */
    Boolean create(Namespace namespace);

    /**
     * 更新命名空间信息
     * @param namespaceName namespaceName
     * @param namespace Namespace
     * @return Boolean
     */
    Boolean update(String namespaceName, Namespace namespace);

    /**
     * 删除Namespace
     * @param namespace 名称
     * @return Boolean
     */
    Boolean delete(String namespace);

    /**
     * 获取Namespace详情
     * @param namespace 名称
     * @return NamespaceDetailView
     */
    NamespaceDetailView get(String namespace);

}
