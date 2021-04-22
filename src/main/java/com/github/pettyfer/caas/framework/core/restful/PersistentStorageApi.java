package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizPersistentStorage;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageListView;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageSelectView;
import com.github.pettyfer.caas.framework.core.service.IPersistentStorageCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/persistent-storage")
public class PersistentStorageApi {

    private final IPersistentStorageCoreService persistentStorageCoreService;

    public PersistentStorageApi(IPersistentStorageCoreService persistentStorageCoreService) {
        this.persistentStorageCoreService = persistentStorageCoreService;
    }

    @GetMapping("page/{namespaceId}")
    public R<IPage<PersistentStorageListView>> page(@PathVariable String namespaceId, BizPersistentStorage persistentStorage, Page<BizPersistentStorage> page) {
        return new R<IPage<PersistentStorageListView>>(persistentStorageCoreService.page(namespaceId, persistentStorage, page));
    }

    @GetMapping("/{namespaceId}/{id}")
    public R<BizPersistentStorage> get(@PathVariable String namespaceId ,@PathVariable String id) {
        return new R<BizPersistentStorage>(persistentStorageCoreService.get(namespaceId, id));
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody BizPersistentStorage persistentStorage) {
        return new R<Boolean>(persistentStorageCoreService.update(persistentStorage));
    }

    @PostMapping
    public R<String> create(@Valid @RequestBody BizPersistentStorage persistentStorage) {
        return new R<String>(persistentStorageCoreService.create(persistentStorage));
    }

    @GetMapping("select/{namespaceId}")
    public R<List<PersistentStorageSelectView>> configSelect(@PathVariable String namespaceId) {
        return new R<List<PersistentStorageSelectView>>(persistentStorageCoreService.select(namespaceId));
    }

}
