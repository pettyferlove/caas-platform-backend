package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.system.entity.SystemOauthClientDetails;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 终端信息表 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
public interface ISystemOauthClientDetailsService extends IService<SystemOauthClientDetails> {

    /**
     * List查找
     *
     * @param systemOauthClientDetails 查询参数对象
     * @param page     Page分页对象
     * @return IPage 返回结果
     */
    IPage<SystemOauthClientDetails> page(SystemOauthClientDetails systemOauthClientDetails, Page<SystemOauthClientDetails> page);

    /**
     * 通过Id查询SystemOauthClientDetails信息
     *
     * @param id 业务主键
     * @return 对象
     */
    SystemOauthClientDetails get(String id);

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
      * @param systemOauthClientDetails 要创建的对象
      * @return Boolean
      */
     String create(SystemOauthClientDetails systemOauthClientDetails);

     /**
      * 更新数据（必须带Id）
      *
      * @param systemOauthClientDetails 对象
      * @return Boolean
      */
     Boolean update(SystemOauthClientDetails systemOauthClientDetails);

}
