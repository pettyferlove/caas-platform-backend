package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;
import com.github.pettyfer.caas.framework.biz.entity.BizKeywordMap;
import com.github.pettyfer.caas.framework.biz.service.IBizKeywordMapService;
import com.github.pettyfer.caas.framework.biz.service.IBizKeywordService;
import com.github.pettyfer.caas.framework.core.service.IKeywordCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class KeywordCoreServiceImpl implements IKeywordCoreService {

    private final IBizKeywordService bizKeywordService;

    private final IBizKeywordMapService bizKeywordMapService;

    public KeywordCoreServiceImpl(IBizKeywordService bizKeywordService, IBizKeywordMapService bizKeywordMapService) {
        this.bizKeywordService = bizKeywordService;
        this.bizKeywordMapService = bizKeywordMapService;
    }

    @Override
    public Boolean update(BizKeyword keyword) {
        return bizKeywordService.update(keyword);
    }

    @Override
    public String create(BizKeyword keyword) {
        return bizKeywordService.create(keyword);
    }

    @Override
    public List<BizKeyword> list() {
        return bizKeywordService.list();
    }

    @Override
    public List<BizKeyword> selected(String bizId, String bizType) {
        return bizKeywordService.selected(bizId, bizType);
    }

    @Override
    public List<BizKeyword> allSelected() {
        return bizKeywordService.allSelected();
    }

    @Override
    public Boolean map(String keywordIds, String bizId, String bizType) {
        bizKeywordMapService.remove(Wrappers.<BizKeywordMap>lambdaQuery().eq(BizKeywordMap::getBizId, bizId).eq(BizKeywordMap::getBizType, bizType));
        if (StrUtil.isNotEmpty(keywordIds)) {
            String[] strings = keywordIds.split(",");
            List<BizKeywordMap> maps = new ArrayList<>();
            for (String id : strings) {
                BizKeywordMap map = new BizKeywordMap();
                map.setKeywordId(id);
                map.setBizId(bizId);
                map.setBizType(bizType);
                maps.add(map);
            }
            bizKeywordMapService.saveBatch(maps);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean delete(String id) {
        bizKeywordService.removeById(id);
        bizKeywordMapService.remove(Wrappers.<BizKeywordMap>lambdaQuery().eq(BizKeywordMap::getKeywordId, id));
        return true;
    }
}
