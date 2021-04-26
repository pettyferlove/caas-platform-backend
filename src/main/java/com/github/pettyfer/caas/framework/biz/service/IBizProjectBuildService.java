package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuild;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildListView;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildSelect;

import java.util.List;

/**
 * <p>
 * 持续部署（CD） 服务类
 * </p>
 *
 * @author Petty
 * @since 2020-07-03
 */
public interface IBizProjectBuildService extends IService<BizProjectBuild> {

    /**
     * List查找
     *
     * @param bizProjectBuild 查询参数对象
     * @param page     Page分页对象
     * @return IPage 返回结果
     */
    IPage<ProjectBuildListView> page(BizProjectBuild bizProjectBuild, Page<BizProjectBuild> page);

    /**
     * 通过Id查询SystemContinuousDeployment信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizProjectBuild get(String id);

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
     * @param bizProjectBuild 要创建的对象
     * @return Boolean
     */
    String create(BizProjectBuild bizProjectBuild);

    /**
     * 更新数据（必须带Id）
     *
     * @param projectBuildDetailView 对象
     * @return Boolean
     */
    Boolean update(BizProjectBuild bizProjectBuild);

    /**
     * 获取自动构建下拉框数据
     * @return 集合
     */
    List<ProjectBuildSelect> select(Integer envType, String namespaceId);

    /**
     * 更新自动构建触发状态
     * @param id ID
     * @param status 当前状态
     * @return Boolean
     */
    Boolean updateStatus(String id, int status);
}
