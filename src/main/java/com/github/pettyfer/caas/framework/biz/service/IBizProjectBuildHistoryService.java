package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildHistorySelectView;

import java.util.List;

/**
 * <p>
 * 项目构建历史记录 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-08
 */
public interface IBizProjectBuildHistoryService extends IService<BizProjectBuildHistory> {

    /**
     * List查找
     *
     * @param bizProjectBuildHistory 查询参数对象
     * @param page                   Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizProjectBuildHistory> page(BizProjectBuildHistory bizProjectBuildHistory, Page<BizProjectBuildHistory> page);

    /**
     * 通过Id查询BizProjectBuildHistory信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizProjectBuildHistory get(String id);

    /**
     * 通过Id删除信息
     *
     * @param id 业务主键
     * @return Boolean
     */
    Boolean delete(String id);

    /**
     * 创建数据
     *
     * @param bizProjectBuildHistory 要创建的对象
     * @return Boolean
     */
    String create(BizProjectBuildHistory bizProjectBuildHistory);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizProjectBuildHistory 对象
     * @return Boolean
     */
    Boolean update(BizProjectBuildHistory bizProjectBuildHistory);

    List<ProjectBuildHistorySelectView> historySelect(String id);

}
