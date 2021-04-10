package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.github.pettyfer.caas.global.constants.BuildStatus;
import com.github.pettyfer.caas.framework.core.model.BuildStepView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface ISqlBuildCoreService {

    IPage<SqlBuildListView> page(BizSqlBuild sqlBuild, Page<BizSqlBuild> page);

    BizSqlBuild get(String id);

    Boolean update(BizSqlBuild sqlBuild);

    String create(BizSqlBuild sqlBuild);

    Boolean startBuild(String id);

    List<SqlBuildHistorySelectView> historySelect(String id);

    void updateStatus(String jobId, BuildStatus status);

    IPage<BizSqlBuildHistory> page(String buildId, BizSqlBuildHistory history, Page<BizSqlBuildHistory> page);

    BuildStepView logStep(String buildId, String jobId);

    Boolean deleteHistory(String historyId);
}
