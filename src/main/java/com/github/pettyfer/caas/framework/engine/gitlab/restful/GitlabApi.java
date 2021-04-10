package com.github.pettyfer.caas.framework.engine.gitlab.restful;

import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabBranchView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabProjectView;
import com.github.pettyfer.caas.framework.engine.gitlab.model.GitlabTagView;
import com.github.pettyfer.caas.framework.engine.gitlab.service.IGitlabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Petty
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/gitlab")
@Api(value = "Gitlab Project Api", tags = {"Gitlab Project Api"})
public class GitlabApi {

    private final IGitlabService gitlabService;

    public GitlabApi(IGitlabService gitlabService) {
        this.gitlabService = gitlabService;
    }

    @ApiOperation(value = "查询全部项目")
    @GetMapping("projects/all")
    public R<List<GitlabProjectView>> allProjects() {
        return new R<>(gitlabService.queryAllProjects());
    }

    @ApiOperation(value = "搜索项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "关键词", dataTypeClass = String.class),
    })
    @GetMapping("projects/search")
    public R<List<GitlabProjectView>> searchProjects(String name) {
        return new R<>(gitlabService.searchProjects(name));
    }

    @ApiOperation(value = "查询项目分支")
    @GetMapping("/{projectId}/branches")
    public R<List<GitlabBranchView>> projectBranches(@PathVariable String projectId) {
        return new R<>(gitlabService.queryProjectBranches(projectId));
    }

    @ApiOperation(value = "查询项目标签")
    @GetMapping("/{projectId}/tags")
    public R<List<GitlabTagView>> projectTags(@PathVariable String projectId) {
        return new R<>(gitlabService.queryProjectsTags(projectId));
    }



}
