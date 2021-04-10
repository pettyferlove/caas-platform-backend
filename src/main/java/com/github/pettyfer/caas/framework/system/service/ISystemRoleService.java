package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.system.entity.SystemRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
public interface ISystemRoleService extends IService<SystemRole> {

    /**
     * List查找
     *
     * @param systemRole 查询参数对象
     * @param page     Page分页对象
     * @return IPage 返回结果
     */
    IPage<SystemRole> page(SystemRole systemRole, Page<SystemRole> page);

    /**
     * 通过Id查询SystemRole信息
     *
     * @param id 业务主键
     * @return 对象
     */
    SystemRole get(String id);

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
      * @param systemRole 要创建的对象
      * @return Boolean
      */
     String create(SystemRole systemRole);

     /**
      * 更新数据（必须带Id）
      *
      * @param systemRole 对象
      * @return Boolean
      */
     Boolean update(SystemRole systemRole);

}
