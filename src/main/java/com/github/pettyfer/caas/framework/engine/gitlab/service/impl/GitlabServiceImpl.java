package com.github.pettyfer.caas.framework.engine.gitlab.service.impl;

import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
import com.google.common.base.Preconditions;
import com.github.pettyfer.caas.framework.core.model.UserConfiguration;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabBranchView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabProjectView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabTagView;
import com.github.pettyfer.caas.framework.engine.gitlab.service.IGitlabService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabTag;
import org.gitlab.api.query.ProjectsQuery;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Petty
 */
@Slf4j
@Service
public class GitlabServiceImpl implements IGitlabService {

    private final IBizUserConfigurationService bizUserConfigurationService;

    public GitlabServiceImpl(IBizUserConfigurationService bizUserConfigurationService) {
        this.bizUserConfigurationService = bizUserConfigurationService;
    }


    @Override
    public List<GitlabProjectView> queryAllProjects() {
        try {
            List<GitlabProject> allProjects = this.loadGitlabApi().getProjectsWithPagination(1, 20);
            return getGitlabProjectViews(allProjects);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询项目错误");
        }
    }

    @Override
    public List<GitlabBranchView> queryProjectBranches(String projectId) {
        try {
            List<GitlabBranch> branches = this.loadGitlabApi().getBranches(projectId);
            return getGitlabProjectBranches(branches);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询项目分支错误");
        }
    }

    @Override
    public List<GitlabTagView> queryProjectsTags(String projectId) {
        try {
            List<GitlabTag> tags = this.loadGitlabApi().getTags(projectId);
            return getGitlabProjectTags(tags);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询项目标签错误");
        }
    }

    @Override
    public List<GitlabProjectView> searchProjects(String name) {
        try {
            ProjectsQuery query = new ProjectsQuery();
            query.setSearch(name);
            List<GitlabProject> queryProjects = this.loadGitlabApi().getProjects(query);
            return getGitlabProjectViews(queryProjects);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("查询项目列表错误");
        }
    }


    private GitlabAPI loadGitlabApi() {
        UserConfiguration userConfiguration = bizUserConfigurationService.loadConfig();
        Preconditions.checkNotNull(userConfiguration.getGitlabHomePath(), "未配置Gitlab地址");
        Preconditions.checkNotNull(userConfiguration.getGitlabApiToken(), "未配置Gitlab Api Token");
        return GitlabAPI.connect(userConfiguration.getGitlabHomePath(), userConfiguration.getGitlabApiToken());
    }

    private List<GitlabBranchView> getGitlabProjectBranches(List<GitlabBranch> queryBranches) {
        return queryBranches.stream().map(i -> GitlabBranchView.builder()
                .name(i.getName())
                .commit(i.getCommit())
                .branchProtected(i.isProtected())
                .build()
        ).collect(Collectors.toList());
    }

    private List<GitlabTagView> getGitlabProjectTags(List<GitlabTag> queryTags) {
        return queryTags.stream().map(i -> GitlabTagView.builder()
                .name(i.getName())
                .commit(i.getCommit())
                .message(i.getMessage())
                .release(i.getRelease())
                .build()
        ).collect(Collectors.toList());
    }

    private List<GitlabProjectView> getGitlabProjectViews(List<GitlabProject> queryProjects) {
        return queryProjects.stream().map(i -> GitlabProjectView.builder()
                .id(i.getId())
                .name(i.getName())
                .owner(i.getOwner())
                .nameWithNamespace(i.getNameWithNamespace())
                .description(i.getDescription())
                .defaultBranch(i.getDefaultBranch())
                .path(i.getPath())
                .pathWithNamespace(i.getPathWithNamespace())
                .createdAt(i.getCreatedAt())
                .sshUrl(i.getSshUrl())
                .httpUrl(i.getSshUrl())
                .webUrl(i.getWebUrl())
                .archived(i.isArchived())
                .lastActivityAt(i.getLastActivityAt())
                .build()).collect(Collectors.toList());
    }
}
