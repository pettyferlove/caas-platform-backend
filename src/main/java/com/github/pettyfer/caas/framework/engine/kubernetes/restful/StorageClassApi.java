package com.github.pettyfer.caas.framework.engine.kubernetes.restful;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.StorageClassView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IStorageClassService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.Page;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/kubernetes/storage-class")
public class StorageClassApi {

    private final IStorageClassService storageClassService;

    public StorageClassApi(IStorageClassService storageClassService) {
        this.storageClassService = storageClassService;
    }

    @GetMapping("list")
    public R<List<StorageClassView>> list() {
        return new R<>(storageClassService.list());
    }

    @GetMapping("page")
    public R<Page<StorageClassView>> page(String name, ListQueryParams params) {
        return new R<Page<StorageClassView>>(storageClassService.page(name, params));
    }

}
