package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentListView;

/**
 * @author Pettyfer
 */
public interface IApplicationDeploymentCoreService {

    /**
     * 创建应用部署项目，同时将会部署至Kubernetes
     *
     * @param namespaceId        命名空间
     * @param deploymentDetail 部署信息详情
     * @return ID
     */
    String create(String namespaceId, ApplicationDeploymentDetailView deploymentDetail);

    /**
     * 更新应用部署项目，同时将会更新至Kubernetes
     *
     * @param namespaceId        命名空间
     * @param deploymentDetail 部署信息详情
     * @return ID
     */
    Boolean update(String namespaceId, ApplicationDeploymentDetailView deploymentDetail);

    /**
     * 获取应用列表
     *
     * @param namespaceId                   命名空间
     * @param applicationDeploymentListView 查询参数
     * @param page                        分页参数
     * @return 集合
     */
    IPage<ApplicationDeploymentListView> page(String namespaceId, ApplicationDeploymentListView applicationDeploymentListView, Page<ApplicationDeploymentListView> page);

    /**
     * 删除应用
     * @param namespaceId 命令空间
     * @param id 应用ID
     */
    void delete(String namespaceId, String id);

    /**
     * 获取应用详情
     * @param id ID
     * @param namespaceId                   命名空间
     * @return 详情
     */
    ApplicationDeploymentDetailView get(String namespaceId, String id);

    /**
     * 自动部署
     * @param autoBuildId autoBuildId
     */
    void autoDeployment(String autoBuildId, String imageName, String tag);
}
