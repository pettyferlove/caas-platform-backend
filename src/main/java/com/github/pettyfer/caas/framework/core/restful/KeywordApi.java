package com.github.pettyfer.caas.framework.core.restful;

import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;
import com.github.pettyfer.caas.framework.core.service.IKeywordCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/keyword")
public class KeywordApi {

    private final IKeywordCoreService keywordCoreService;

    public KeywordApi(IKeywordCoreService keywordCoreService) {
        this.keywordCoreService = keywordCoreService;
    }

    @GetMapping("list")
    public R<List<BizKeyword>> list() {
        return new R<List<BizKeyword>>(keywordCoreService.list());
    }


    @GetMapping("selected/{bizType}/{bizId}")
    public R<List<BizKeyword>> selected(@PathVariable String bizId, @PathVariable String bizType) {
        return new R<List<BizKeyword>>(keywordCoreService.selected(bizId, bizType));
    }

    @GetMapping("all-selected")
    public R<List<BizKeyword>> allSelected() {
        return new R<List<BizKeyword>>(keywordCoreService.allSelected());
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody BizKeyword keyword) {
        return new R<Boolean>(keywordCoreService.update(keyword));
    }

    @PostMapping
    public R<String> create(@Valid @RequestBody BizKeyword keyword) {
        return new R<String>(keywordCoreService.create(keyword));
    }

    @DeleteMapping("{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return new R<Boolean>(keywordCoreService.delete(id));
    }

}
