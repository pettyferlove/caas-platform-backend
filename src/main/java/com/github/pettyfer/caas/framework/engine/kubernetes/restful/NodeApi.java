package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NodeDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INodeService;
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
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/node")
@Api(value = "Node Api", tags = {"Node Api"})
public class NodeApi {

    private final INodeService nodeService;

    public NodeApi(INodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("list")
    @ApiOperation(value = "查询Node列表")
    public R<List<NodeDetailView>> list() {
        return new R<>(nodeService.list());
    }

    @GetMapping("{nodeName}")
    @ApiOperation(value = "获取Node详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "nodeName", value = "nodeName", dataTypeClass = String.class)
    })
    public R<NodeDetailView> get(@PathVariable String nodeName) {
        return new R<>(nodeService.get(nodeName));
    }

}
