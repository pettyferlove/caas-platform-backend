package com.github.pettyfer.caas.framework.core.restful;


import com.github.pettyfer.caas.framework.biz.entity.BizUserConfiguration;
import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
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
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/user-configuration")
public class UserConfigurationApi {

    private final IBizUserConfigurationService bizUserConfigurationService;

    public UserConfigurationApi(IBizUserConfigurationService bizUserConfigurationService) {
        this.bizUserConfigurationService = bizUserConfigurationService;
    }

    @GetMapping
    public R<BizUserConfiguration> get() {
        return new R<>(bizUserConfigurationService.get());
    }

    @PostMapping
    public R<String> create(BizUserConfiguration bizUserConfiguration) {
        return new R<>(bizUserConfigurationService.create(bizUserConfiguration));
    }

    @PutMapping
    public R<Boolean> update(BizUserConfiguration bizUserConfiguration) {
        return new R<>(bizUserConfigurationService.update(bizUserConfiguration));
    }

    @PutMapping("refresh")
    public R<Boolean> refresh() {
        return new R<Boolean>(bizUserConfigurationService.refresh());
    }

    @GetMapping("check")
    public R<Boolean> checkConfiguration() {
        return new R<>(bizUserConfigurationService.checkConfiguration());
    }

}
