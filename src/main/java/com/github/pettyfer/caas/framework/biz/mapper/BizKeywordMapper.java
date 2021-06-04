package com.github.pettyfer.caas.framework.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-06-02
 */
public interface BizKeywordMapper extends BaseMapper<BizKeyword> {

    List<BizKeyword> selected(String bizId, String bizType);

    List<BizKeyword> allSelected();

}
