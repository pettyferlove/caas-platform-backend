package com.github.pettyfer.caas.framework.engine.gitlab.service;

import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabBranchView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabProjectView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabTagView;

import java.util.List;

/**
 * Gitlab项目信息查询
 *
 * @author admin
 */
public interface IGitlabService {

    /**
     * 获取当前Gitlab用户下的全部项目
     *
     * @return Project集合
     */
    List<GitlabProjectView> queryAllProjects();

    /**
     * 获取项目的全部分支
     *
     * @param projectId 项目ID
     * @return Branch集合
     */
    List<GitlabBranchView> queryProjectBranches(String projectId);

    /**
     * 获取项目的全部Tag
     *
     * @param projectId 项目ID
     * @return Tag集合
     */
    List<GitlabTagView> queryProjectsTags(String projectId);

    /**
     * 根据名称搜索数据
     *
     * @param name 项目名称
     * @return 搜索结果
     */
    List<GitlabProjectView> searchProjects(String name);
}
