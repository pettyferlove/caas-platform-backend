package com.github.pettyfer.caas.framework.engine.docker.register.restful;

import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.engine.docker.register.model.RepositoryTagView;
import com.github.pettyfer.caas.framework.engine.docker.register.model.RepositoryView;
import com.github.pettyfer.caas.framework.engine.docker.register.service.IHarborService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Petty
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/docker/register/harbor")
@Api(value = "Harbor镜像仓库 Api", tags = {"Harbor镜像仓库 Api"})
public class HarborApi {

    private final IHarborService harborService;

    public HarborApi(IHarborService harborService) {
        this.harborService = harborService;
    }

    @ApiOperation(value = "搜索公开资源")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "q", value = "关键词", dataTypeClass = String.class),
    })
    @GetMapping("public/search")
    public R<List<RepositoryView>> searchPublicRepository(String q) {
        return new R<>(harborService.searchPublicRepository(q));
    }

    @ApiOperation(value = "查询镜像标签")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "repoName", value = "Repository Name", dataTypeClass = String.class)
    })
    @GetMapping("repository/{repoName}/tag")
    public R<List<RepositoryTagView>> queryRepositoryTag(@PathVariable String repoName) {
        return new R<>(harborService.queryRepositoryTag(repoName));
    }

    @ApiOperation(value = "搜索公开资源")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "仓库ID", dataTypeClass = String.class),
            @ApiImplicitParam(paramType = "query", name = "q", value = "关键词", dataTypeClass = String.class),
    })
    @GetMapping("query")
    public R<List<RepositoryView>> queryRepository(@RequestParam String projectId, String q) {
        return new R<>(harborService.queryRepository(projectId, q));
    }

}
