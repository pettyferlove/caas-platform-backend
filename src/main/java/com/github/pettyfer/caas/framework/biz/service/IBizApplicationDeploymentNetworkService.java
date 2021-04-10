package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentNetworkView;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentNetwork;

import java.util.List;

/**
 * <p>
 * 应用网络设置 服务类
 * </p>
 *
 * @author Petty
 * @since 2020-07-28
 */
public interface IBizApplicationDeploymentNetworkService extends IService<BizApplicationDeploymentNetwork> {

    /**
     * List查找
     *
     * @param bizApplicationDeploymentNetwork 查询参数对象
     * @param page                               Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizApplicationDeploymentNetwork> page(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork, Page<BizApplicationDeploymentNetwork> page);

    /**
     * 通过Id查询SystemApplicationDeploymentNetwork信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizApplicationDeploymentNetwork get(String id);

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
     * @param bizApplicationDeploymentNetwork 要创建的对象
     * @return Boolean
     */
    String create(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizApplicationDeploymentNetwork 对象
     * @return Boolean
     */
    Boolean update(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork);

    /**
     * 批量插入网络信息
     * @param deploymentId ID
     * @param deploymentNetworks 网络集合
     */
    void batchInsert(String deploymentId, List<ApplicationDeploymentNetworkView> deploymentNetworks);

}
