package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.ISecretService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.Secret;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/secret")
public class SecretApi {

    private final ISecretService secretService;

    public SecretApi(ISecretService secretService) {
        this.secretService = secretService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<Secret> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<Secret>(secretService.get(namespace, name));
    }

}
