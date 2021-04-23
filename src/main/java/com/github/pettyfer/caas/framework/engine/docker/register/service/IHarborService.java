package com.github.pettyfer.caas.framework.engine.docker.register.service;

import com.github.pettyfer.caas.framework.engine.docker.register.model.ProjectCreate;
import com.github.pettyfer.caas.framework.engine.docker.register.model.RepositoryTagView;
import com.github.pettyfer.caas.framework.engine.docker.register.model.RepositoryView;

import java.util.List;

/**
 * @author Petty
 */
public interface IHarborService {

    /**
     * 查询公共仓库
     *
     * @param q 查询关键词
     * @return RepositoryView 视图对象
     */
    List<RepositoryView> searchPublicRepository(String q);

    /**
     * 查询仓库中的镜像
     *
     * @param projectId 项目ID
     * @param q         查询关键字
     * @return RepositoryView 视图对象
     */
    List<RepositoryView> queryRepository(String projectId, String q);

    /**
     * 获取Repository Tag
     *
     * @param repoName Repository名称
     * @return 集合
     */
    List<RepositoryTagView> queryRepositoryTag(String repoName);

    /**
     * 检查Project Name是否重复
     *
     * @param projectName 用户定义的Project Name 对应前端镜像仓库别名
     * @return Boolean true project已存在 false 不存在
     */
    Boolean checkProject(String projectName);

    /**
     * 创建一个镜像仓库Project
     *
     * @param project Project实体
     * @return project id
     */
    String createProject(ProjectCreate project);

    /**
     * 创建一个镜像仓库Project
     *
     * @param projectName ProjectName
     * @return project id
     */
    String createProject(String projectName);

    /**
     * 删除镜像仓库
     *
     * @param projectId project id
     * @return Boolean
     */
    Boolean removeProject(String projectId);

}
