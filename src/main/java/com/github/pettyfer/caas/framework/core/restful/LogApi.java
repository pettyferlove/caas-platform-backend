package com.github.pettyfer.caas.framework.core.restful;

import com.github.pettyfer.caas.framework.core.service.ILogCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/log")
public class LogApi {

    private final ILogCoreService logCoreService;

    public LogApi(ILogCoreService logCoreService) {
        this.logCoreService = logCoreService;
    }

    @GetMapping("build/{namespaceId}/{podName}/{containerName}")
    public R<String> log(@PathVariable String namespaceId, @PathVariable String podName, @PathVariable String containerName) {
        return new R<String>(logCoreService.log(namespaceId, podName, containerName));
    }

}
