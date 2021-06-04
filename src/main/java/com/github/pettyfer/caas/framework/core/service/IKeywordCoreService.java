package com.github.pettyfer.caas.framework.core.service;

import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface IKeywordCoreService {

    /**
     * 更新关键词
     * @param keyword 关键词信息
     * @return 是否成功
     */
    Boolean update(BizKeyword keyword);


    /**
     * 创建一个新的关键词
     * @param keyword 关键词信息
     * @return 关键词ID
     */
    String create(BizKeyword keyword);

    List<BizKeyword> list();

    List<BizKeyword> selected(String bizId, String bizType);

    List<BizKeyword> allSelected();

    Boolean map(String keywordIds, String bizId, String bizType);

    Boolean delete(String id);
}
