package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;

import java.util.List;

/**
 * <p>
 * SQL构建历史记录 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
public interface IBizSqlBuildHistoryService extends IService<BizSqlBuildHistory> {

    /**
     * List查找
     *
     * @param bizSqlBuildHistory 查询参数对象
     * @param page               Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizSqlBuildHistory> page(BizSqlBuildHistory bizSqlBuildHistory, Page<BizSqlBuildHistory> page);

    /**
     * 通过Id查询BizSqlBuildHistory信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizSqlBuildHistory get(String id);

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
     * @param bizSqlBuildHistory 要创建的对象
     * @return Boolean
     */
    String create(BizSqlBuildHistory bizSqlBuildHistory);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizSqlBuildHistory 对象
     * @return Boolean
     */
    Boolean update(BizSqlBuildHistory bizSqlBuildHistory);

    List<SqlBuildHistorySelectView> historySelect(String id);
}
