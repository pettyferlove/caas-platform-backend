package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/deployment")
public class DeploymentApi {

    private final IDeploymentService deploymentService;

    public DeploymentApi(IDeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<Deployment> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<>(deploymentService.get(namespace, name));
    }

}
