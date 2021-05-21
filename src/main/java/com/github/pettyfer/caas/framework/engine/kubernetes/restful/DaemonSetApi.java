package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDaemonSetService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/daemon-set")
public class DaemonSetApi {

    private final IDaemonSetService daemonSetService;

    public DaemonSetApi(IDaemonSetService daemonSetService) {
        this.daemonSetService = daemonSetService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<DaemonSet> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<>(daemonSetService.get(namespace, name));
    }

}
