package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.core.service.IPodCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.PodPageView;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/pod")
public class PodApi {

    private final IPodCoreService podCoreService;

    public PodApi(IPodCoreService podCoreService) {
        this.podCoreService = podCoreService;
    }

    @GetMapping("page")
    public R<Page<PodPageView>> page(@RequestParam String namespaceId, @RequestParam String deploymentId, ListQueryParams params) {
        return new R<Page<PodPageView>>(podCoreService.page(namespaceId, deploymentId, params));
    }

    @DeleteMapping("{namespaceId}/{deploymentId}")
    public R<Boolean> delete(@PathVariable String namespaceId, @PathVariable String deploymentId) {
        return new R<Boolean>(podCoreService.delete(namespaceId, deploymentId));
    }

}
