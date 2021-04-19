package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.system.entity.SystemUser;
import com.github.pettyfer.caas.framework.system.model.UserDetailsView;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
public interface ISystemUserService extends IService<SystemUser> {

    /**
     * List查找
     *
     * @param systemUser 查询参数对象
     * @param page       Page分页对象
     * @return IPage 返回结果
     */
    IPage<SystemUser> page(SystemUser systemUser, Page<SystemUser> page);

    /**
     * 通过Id查询SystemUser信息
     *
     * @param id 业务主键
     * @return 对象
     */
    UserDetailsView get(String id);

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
     * @param systemUser 要创建的对象
     * @return Boolean
     */
    String create(UserDetailsView systemUser);

    /**
     * 更新数据（必须带Id）
     *
     * @param systemUser 对象
     * @return Boolean
     */
    Boolean update(UserDetailsView systemUser);
}
