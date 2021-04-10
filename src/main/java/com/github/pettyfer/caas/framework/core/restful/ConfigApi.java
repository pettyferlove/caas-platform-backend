package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.core.service.IConfigCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/config")
public class ConfigApi {

    private final IConfigCoreService configCoreService;

    public ConfigApi(IConfigCoreService configCoreService) {
        this.configCoreService = configCoreService;
    }

    @GetMapping("page")
    public R<IPage<ConfigListView>> page(BizConfig config, Page<BizConfig> page) {
        return new R<IPage<ConfigListView>>(configCoreService.page(config, page));
    }

    @GetMapping("/{id}")
    public R<BizConfig> get(@PathVariable String id) {
        return new R<BizConfig>(configCoreService.get(id));
    }

    @PutMapping
    public R<Boolean> update(@Valid BizConfig config) {
        return new R<Boolean>(configCoreService.update(config));
    }

    @PostMapping
    public R<String> create(@Valid BizConfig config) {
        return new R<String>(configCoreService.create(config));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return new R<Boolean>(configCoreService.delete(id));
    }


}
