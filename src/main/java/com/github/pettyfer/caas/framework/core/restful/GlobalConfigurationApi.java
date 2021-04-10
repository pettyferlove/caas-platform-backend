package com.github.pettyfer.caas.framework.core.restful;


import com.github.pettyfer.caas.framework.biz.entity.BizGlobalConfiguration;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 接口控制器
 * </p>
 *
 * @author Petty
 * @since 2020-06-29
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/global-configuration")
public class GlobalConfigurationApi {
    private final IBizGlobalConfigurationService bizGlobalConfigurationService;

    public GlobalConfigurationApi(IBizGlobalConfigurationService bizGlobalConfigurationService) {
        this.bizGlobalConfigurationService = bizGlobalConfigurationService;
    }

    @GetMapping
    public R<BizGlobalConfiguration> get() {
        return new R<>(bizGlobalConfigurationService.get());
    }

    @PostMapping
    public R<String> create(BizGlobalConfiguration bizGlobalConfiguration) {
        return new R<>(bizGlobalConfigurationService.create(bizGlobalConfiguration));
    }

    @PutMapping
    public R<Boolean> update(BizGlobalConfiguration bizGlobalConfiguration) {
        return new R<>(bizGlobalConfigurationService.update(bizGlobalConfiguration));
    }
}
