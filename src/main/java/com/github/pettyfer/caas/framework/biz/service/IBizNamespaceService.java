package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-03-22
 */
public interface IBizNamespaceService extends IService<BizNamespace> {

    /**
     * List查找
     *
     * @param bizNamespace 查询参数对象
     * @param page         Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizNamespace> page(BizNamespace bizNamespace, Page<BizNamespace> page);

    /**
     * 通过Id查询BizNamespace信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizNamespace get(String id);

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
     * @param bizNamespace 要创建的对象
     * @return Boolean
     */
    String create(BizNamespace bizNamespace);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizNamespace 对象
     * @return Boolean
     */
    Boolean update(BizNamespace bizNamespace);

    Boolean checkNamespace();

}
