package com.github.pettyfer.caas.framework.core.restful;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.github.pettyfer.caas.framework.biz.service.IBizProjectBuildService;
import com.github.pettyfer.caas.framework.core.model.BuildStepView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildHistorySelectView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildListView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildSelect;
import com.github.pettyfer.caas.framework.core.service.IProjectBuildCoreService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Petty
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/core/project-build")
public class ProjectBuildApi {

    private final IBizProjectBuildService bizProjectBuildService;

    private final IProjectBuildCoreService projectBuildCoreService;

    public ProjectBuildApi(IBizProjectBuildService bizProjectBuildService, IProjectBuildCoreService projectBuildCoreService) {
        this.bizProjectBuildService = bizProjectBuildService;
        this.projectBuildCoreService = projectBuildCoreService;
    }

    @GetMapping("page")
    public R<IPage<ProjectBuildListView>> page(BizProjectBuild projectBuild, Page<BizProjectBuild> page) {
        return new R<IPage<ProjectBuildListView>>(projectBuildCoreService.page(projectBuild, page));
    }

    @GetMapping("/{id}")
    public R<BizProjectBuild> get(@PathVariable String id) {
        return new R<>(bizProjectBuildService.get(id));
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody BizProjectBuild projectBuild) {
        return new R<>(bizProjectBuildService.update(projectBuild));
    }

    @PostMapping
    public R<String> create(@Valid @RequestBody BizProjectBuild projectBuild) {
        return new R<>(projectBuildCoreService.create(projectBuild));
    }

    @PutMapping("status")
    public R<Boolean> updateStatus(@RequestParam String id, @RequestParam int status) {
        return new R<>(bizProjectBuildService.updateStatus(id, status));
    }

    @PostMapping("build/{id}")
    public R<Boolean> build(@PathVariable String id) {
        projectBuildCoreService.manualBuild(id);
        return new R<>();
    }


    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return new R<>(projectBuildCoreService.delete(id));
    }

    @GetMapping("/select/{namespaceId}")
    public R<List<ProjectBuildSelect>> select(Integer envType, String currentId, @PathVariable String namespaceId) {
        return new R<>(bizProjectBuildService.select(envType, currentId, namespaceId));
    }


    @GetMapping("history/select/{id}")
    public R<List<ProjectBuildHistorySelectView>> historySelect(@PathVariable String id) {
        return new R<List<ProjectBuildHistorySelectView>>(projectBuildCoreService.historySelect(id));
    }

    @GetMapping("page/history/{buildId}")
    public R<IPage<BizProjectBuildHistory>> page(@PathVariable String buildId, BizProjectBuildHistory history, Page<BizProjectBuildHistory> page) {
        return new R<IPage<BizProjectBuildHistory>>(projectBuildCoreService.page(buildId, history, page));
    }

    @GetMapping("build/log/step/{buildId}/{jobId}")
    public R<BuildStepView> logStep(@PathVariable String buildId, @PathVariable String jobId) {
        return new R<BuildStepView>(projectBuildCoreService.logStep(buildId, jobId));
    }

    @DeleteMapping("build/log/{historyId}")
    public R<Boolean> deleteHistory(@PathVariable String historyId) {
        return new R<Boolean>(projectBuildCoreService.deleteHistory(historyId));
    }

}
