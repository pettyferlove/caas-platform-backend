package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IIngressService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/ingress")
public class IngressApi {

    private final IIngressService iIngressService;

    public IngressApi(IIngressService iIngressService) {
        this.iIngressService = iIngressService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<Ingress> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<Ingress>(iIngressService.get(namespace, name));
    }

}
