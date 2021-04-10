package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.github.pettyfer.caas.framework.core.service.ISqlBuildCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.framework.core.model.BuildStepView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/sql-build")
public class SqlBuildApi {

    private final ISqlBuildCoreService sqlBuildCoreService;

    public SqlBuildApi(ISqlBuildCoreService sqlBuildCoreService) {
        this.sqlBuildCoreService = sqlBuildCoreService;
    }

    @GetMapping("page")
    public R<IPage<SqlBuildListView>> page(BizSqlBuild sqlBuild, Page<BizSqlBuild> page) {
        return new R<IPage<SqlBuildListView>>(sqlBuildCoreService.page(sqlBuild, page));
    }

    @GetMapping("/{id}")
    public R<BizSqlBuild> get(@PathVariable String id) {
        return new R<BizSqlBuild>(sqlBuildCoreService.get(id));
    }

    @PutMapping
    public R<Boolean> update(@Valid BizSqlBuild sqlBuild) {
        return new R<Boolean>(sqlBuildCoreService.update(sqlBuild));
    }

    @PostMapping
    public R<String> create(@Valid BizSqlBuild sqlBuild) {
        return new R<String>(sqlBuildCoreService.create(sqlBuild));
    }

    @PostMapping("build/{id}")
    public R<Boolean> build(@PathVariable String id) {
        return new R<>(sqlBuildCoreService.startBuild(id));
    }

    @GetMapping("history/select/{id}")
    public R<List<SqlBuildHistorySelectView>> historySelect(@PathVariable String id) {
        return new R<List<SqlBuildHistorySelectView>>(sqlBuildCoreService.historySelect(id));
    }

    @GetMapping("page/history/{buildId}")
    public R<IPage<BizSqlBuildHistory>> page(@PathVariable String buildId, BizSqlBuildHistory history, Page<BizSqlBuildHistory> page) {
        return new R<IPage<BizSqlBuildHistory>>(sqlBuildCoreService.page(buildId, history, page));
    }

    @GetMapping("build/log/step/{buildId}/{jobId}")
    public R<BuildStepView> logStep(@PathVariable String buildId, @PathVariable String jobId) {
        return new R<BuildStepView>(sqlBuildCoreService.logStep(buildId, jobId));
    }

    @DeleteMapping("build/log/{historyId}")
    public R<Boolean> deleteHistory(@PathVariable String historyId) {
        return new R<Boolean>(sqlBuildCoreService.deleteHistory(historyId));
    }


}
