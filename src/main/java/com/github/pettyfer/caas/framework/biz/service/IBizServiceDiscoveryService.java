package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizServiceDiscovery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-20
 */
public interface IBizServiceDiscoveryService extends IService<BizServiceDiscovery> {

    /**
     * List查找
     *
     * @param namespaceId 命名空间ID
     * @param bizServiceDiscovery 查询参数对象
     * @param page       Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizServiceDiscovery> page(String namespaceId, BizServiceDiscovery bizServiceDiscovery, Page<BizServiceDiscovery> page);

    /**
     * 通过Id查询BizNetwork信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizServiceDiscovery get(String id);

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
     * @param bizServiceDiscovery 要创建的对象
     * @return Boolean
     */
    String create(BizServiceDiscovery bizServiceDiscovery);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizServiceDiscovery 对象
     * @return Boolean
     */
    Boolean update(BizServiceDiscovery bizServiceDiscovery);

}
