package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;

/**
 * <p>
 * SQL构建 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
public interface IBizSqlBuildService extends IService<BizSqlBuild> {

    /**
     * List查找
     *
     * @param bizSqlBuild 查询参数对象
     * @param page        Page分页对象
     * @return IPage 返回结果
     */
    IPage<SqlBuildListView> page(BizSqlBuild bizSqlBuild, Page<BizSqlBuild> page);

    /**
     * 通过Id查询BizSqlBuild信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizSqlBuild get(String id);

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
     * @param bizSqlBuild 要创建的对象
     * @return Boolean
     */
    String create(BizSqlBuild bizSqlBuild);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizSqlBuild 对象
     * @return Boolean
     */
    Boolean update(BizSqlBuild bizSqlBuild);

}
