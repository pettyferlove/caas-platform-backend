package com.github.pettyfer.caas.framework.biz.mapper;

import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildHistorySelectView;

import java.util.List;

/**
 * <p>
 * 项目构建历史记录 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-04-08
 */
public interface BizProjectBuildHistoryMapper extends BaseMapper<BizProjectBuildHistory> {

    List<ProjectBuildHistorySelectView> historySelect(String id);

}
