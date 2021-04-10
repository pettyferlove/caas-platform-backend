package com.github.pettyfer.caas.framework.biz.mapper;

import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;

import java.util.List;

/**
 * <p>
 * SQL构建历史记录 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
public interface BizSqlBuildHistoryMapper extends BaseMapper<BizSqlBuildHistory> {

    List<SqlBuildHistorySelectView> historySelect(String id);
}
