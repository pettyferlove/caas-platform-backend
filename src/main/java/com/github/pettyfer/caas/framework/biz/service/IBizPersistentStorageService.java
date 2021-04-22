package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizPersistentStorage;

/**
 * <p>
 * 持久化储存 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-21
 */
public interface IBizPersistentStorageService extends IService<BizPersistentStorage> {

    /**
     * List查找
     *
     * @param bizPersistentStorage 查询参数对象
     * @param page                 Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizPersistentStorage> page(String namespaceId, BizPersistentStorage bizPersistentStorage, Page<BizPersistentStorage> page);

    /**
     * 通过Id查询BizPersistentStorage信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizPersistentStorage get(String id);

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
     * @param bizPersistentStorage 要创建的对象
     * @return Boolean
     */
    String create(BizPersistentStorage bizPersistentStorage);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizPersistentStorage 对象
     * @return Boolean
     */
    Boolean update(BizPersistentStorage bizPersistentStorage);

}
