package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.NodeDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INodeService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
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
public class NodeApi {

    private final INodeService nodeService;

    public NodeApi(INodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("list")
    public R<List<NodeDetailView>> list() {
        return new R<>(nodeService.list());
    }

    @GetMapping("{nodeName}")
    public R<NodeDetailView> get(@PathVariable String nodeName) {
        return new R<>(nodeService.get(nodeName));
    }

}
