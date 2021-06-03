package com.github.pettyfer.caas.framework.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuild;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.github.pettyfer.caas.framework.core.event.GitlabPushDetails;
import com.github.pettyfer.caas.framework.core.model.BuildStepView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildHistorySelectView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildListView;
import com.github.pettyfer.caas.global.constants.BuildStatus;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface IProjectBuildCoreService {

    IPage<ProjectBuildListView> page(BizProjectBuild projectBuild, Page<BizProjectBuild> page);

    /**
     * 创建自动构建配置
     *
     * @param projectBuild 要创建的对象
     * @return Boolean
     */
    String create(BizProjectBuild projectBuild);

    /**
     * 更新自动构建配置
     *
     * @param projectBuild 要更新的对象
     * @return Boolean
     */
    Boolean update(BizProjectBuild projectBuild);

    /**
     * 删除自动构建
     *
     * @param id ID
     * @return 是否成功
     */
    Boolean delete(String id);

    /**
     * 手动触发构建
     *
     * @param id 构建ID
     */
    void manualBuild(String id);

    /**
     * 自动触发构建
     *
     * @param id          构建ID
     * @param pushDetails 代码推送详情
     */
    void autoBuild(String id, GitlabPushDetails pushDetails);

    /**
     * 根据自动构建ID创建源码仓库Hook触发器
     *
     * @param id              持续集成唯一ID
     * @param sourceProjectId 项目ID（由Gitlab Api返回）
     * @return HookID
     */
    String createSourceProjectHook(String id, String sourceProjectId);

    /**
     * 删除源码仓库触发器
     *
     * @param sourceProjectId     源码仓库ID
     * @param sourceProjectHookId 触发器ID
     */
    void removeSourceProjectHook(String sourceProjectId, String sourceProjectHookId);

    String createImagesDepositoryProject();

    void removeImagesDepositoryProject();

    /**
     * 查询历史记录下拉框数据
     *
     * @param id 构建ID
     * @return 集合
     */
    List<ProjectBuildHistorySelectView> historySelect(String id);

    /**
     * 查询历史记录列表
     *
     * @param buildId 构建ID
     * @param history 历史基础查询条件
     * @param page    分页参数
     * @return 分页查询结果
     */
    IPage<BizProjectBuildHistory> page(String buildId, BizProjectBuildHistory history, Page<BizProjectBuildHistory> page);

    BuildStepView logStep(String buildId, String jobId);

    Boolean deleteHistory(String historyId);

    void updateStatus(String jobId, BuildStatus status);


}
