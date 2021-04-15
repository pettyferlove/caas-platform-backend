package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;

/**
 * <p>
 * 应用部署 服务类
 * </p>
 *
 * @author Petty
 * @since 2020-07-28
 */
public interface IBizApplicationDeploymentService extends IService<BizApplicationDeployment> {

    /**
     * List查找
     *
     *
     * @param namespace
     * @param bizApplicationDeployment 查询参数对象
     * @param page                        Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizApplicationDeployment> page(String namespaceId, BizApplicationDeployment bizApplicationDeployment, Page<BizApplicationDeployment> page);

    /**
     * 通过Id查询SystemApplicationDeployment信息
     *
     * @param id 业务主键
     * @return 对象
     */
    ApplicationDeploymentDetailView get(String id);

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
     * @param namespace 命名空间
     * @param bizApplicationDeployment 要创建的对象
     * @return Boolean
     */
    String create(String namespaceId, BizApplicationDeployment bizApplicationDeployment);

    /**
     * 更新数据（必须带Id）
     * @param namespace 命名空间
     * @param bizApplicationDeployment 对象
     * @return Boolean
     */
    Boolean update(String namespaceId, BizApplicationDeployment bizApplicationDeployment);

    Boolean update(BizApplicationDeployment bizApplicationDeployment);

    /**
     * 触发自动更新时更新部分应用信息
     * @param bizApplicationDeployment 应用信息
     * @return 是否成功
     */
    Boolean updateForRobot(BizApplicationDeployment bizApplicationDeployment);

}
