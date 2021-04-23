package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.core.service.INamespaceCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Petty
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/namespace")
public class NamespaceApi {

    private final INamespaceCoreService namespaceCoreService;

    public NamespaceApi(INamespaceCoreService namespaceCoreService) {
        this.namespaceCoreService = namespaceCoreService;
    }

    @GetMapping("all/list")
    public R<List<BizNamespace>> listAll() {
        return new R<>(namespaceCoreService.listAll());
    }

    @GetMapping("page")
    public R<IPage<BizNamespace>> page(BizNamespace bizNamespace, Page<BizNamespace> page) {
        return new R<>(namespaceCoreService.page(bizNamespace, page));
    }

    @PostMapping
    public R<Boolean> create(@Valid @RequestBody BizNamespace namespace) {
        return new R<>(namespaceCoreService.create(namespace));
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody BizNamespace namespace) {
        return new R<Boolean>(namespaceCoreService.update(namespace));
    }

    @GetMapping("{id}")
    public R<BizNamespace> get(@PathVariable String id) {
        return new R<BizNamespace>(namespaceCoreService.get(id));
    }

    @GetMapping("/detail/{id}")
    public R<NamespaceDetailView> detail(@PathVariable String id) {
        return new R<>(namespaceCoreService.detail(id));
    }

    @DeleteMapping("{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return new R<>(namespaceCoreService.delete(id));
    }

}
