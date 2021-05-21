package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IStatefulSetService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/stateful-set")
public class StatefulSetApi {

    private final IStatefulSetService statefulSetService;

    public StatefulSetApi(IStatefulSetService statefulSetService) {
        this.statefulSetService = statefulSetService;
    }

    @GetMapping("/{namespace}/{name}")
    public R<StatefulSet> get(@PathVariable String namespace, @PathVariable String name) {
        return new R<StatefulSet>(statefulSetService.get(namespace, name));
    }


}
