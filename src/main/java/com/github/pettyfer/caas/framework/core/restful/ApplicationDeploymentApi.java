package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.core.service.IApplicationDeploymentCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentListView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/application-deployment")
public class ApplicationDeploymentApi {

    private final IApplicationDeploymentCoreService applicationDeploymentCoreService;

    public ApplicationDeploymentApi(IApplicationDeploymentCoreService applicationDeploymentCoreService) {
        this.applicationDeploymentCoreService = applicationDeploymentCoreService;
    }

    @GetMapping("page/{namespaceId}")
    public R<IPage<ApplicationDeploymentListView>> page(@PathVariable String namespaceId, ApplicationDeploymentListView applicationDeploymentListView, Page<ApplicationDeploymentListView> page) {
        return new R<>(applicationDeploymentCoreService.page(namespaceId, applicationDeploymentListView, page));
    }

    @GetMapping("{namespaceId}/{id}")
    public R<ApplicationDeploymentDetailView> get(@PathVariable String namespaceId, @PathVariable String id) {
        return new R<>(applicationDeploymentCoreService.get(namespaceId, id));
    }

    @PostMapping("{namespaceId}")
    public R<String> create(@PathVariable String namespaceId, @RequestBody ApplicationDeploymentDetailView deploymentDetail){
        return new R<>(applicationDeploymentCoreService.create(namespaceId, deploymentDetail));
    }

    @PutMapping("{namespaceId}")
    public R<Boolean> update(@PathVariable String namespaceId, @RequestBody ApplicationDeploymentDetailView deploymentDetail){
        return new R<>(applicationDeploymentCoreService.update(namespaceId, deploymentDetail));
    }

    @DeleteMapping("{namespaceId}/{id}")
    public R<Object> delete(@PathVariable String namespaceId ,@PathVariable String id){
        applicationDeploymentCoreService.delete(namespaceId, id);
        return new R<>();
    }

}
