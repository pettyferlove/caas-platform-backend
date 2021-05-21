package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/service")
public class ServiceApi {

    private final INetworkService networkService;

    public ServiceApi(INetworkService networkService) {
        this.networkService = networkService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<Service> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<Service>(networkService.get(namespace, name));
    }


}
