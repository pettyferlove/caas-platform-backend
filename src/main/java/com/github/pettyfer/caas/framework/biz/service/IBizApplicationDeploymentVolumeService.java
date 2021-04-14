package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentVolume;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-13
 */
public interface IBizApplicationDeploymentVolumeService extends IService<BizApplicationDeploymentVolume> {

    /**
     * List查找
     *
     * @param bizApplicationDeploymentVolume 查询参数对象
     * @param page                           Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizApplicationDeploymentVolume> page(BizApplicationDeploymentVolume bizApplicationDeploymentVolume, Page<BizApplicationDeploymentVolume> page);

    /**
     * 通过Id查询BizApplicationDeploymentVolume信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizApplicationDeploymentVolume get(String id);

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
     * @param bizApplicationDeploymentVolume 要创建的对象
     * @return Boolean
     */
    String create(BizApplicationDeploymentVolume bizApplicationDeploymentVolume);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizApplicationDeploymentVolume 对象
     * @return Boolean
     */
    Boolean update(BizApplicationDeploymentVolume bizApplicationDeploymentVolume);

}
