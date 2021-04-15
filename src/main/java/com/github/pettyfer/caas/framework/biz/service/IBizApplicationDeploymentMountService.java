package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentMount;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentMountView;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-13
 */
public interface IBizApplicationDeploymentMountService extends IService<BizApplicationDeploymentMount> {

    /**
     * List查找
     *
     * @param bizApplicationDeploymentMount 查询参数对象
     * @param page                           Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizApplicationDeploymentMount> page(BizApplicationDeploymentMount bizApplicationDeploymentMount, Page<BizApplicationDeploymentMount> page);

    /**
     * 通过Id查询BizApplicationDeploymentVolume信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizApplicationDeploymentMount get(String id);

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
     * @param bizApplicationDeploymentMount 要创建的对象
     * @return Boolean
     */
    String create(BizApplicationDeploymentMount bizApplicationDeploymentMount);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizApplicationDeploymentMount 对象
     * @return Boolean
     */
    Boolean update(BizApplicationDeploymentMount bizApplicationDeploymentMount);

    /**
     * 批量插入储存挂载配置
     * @param deploymentId 应用ID
     * @param deploymentVolumes 挂载配置
     */
    void batchInsert(String deploymentId, List<ApplicationDeploymentMountView> deploymentMounts);

}
