package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.system.entity.UserListView;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-16
 */
public interface IUserListViewService extends IService<UserListView> {

    /**
     * List查找
     *
     * @param userListView 查询参数对象
     * @param page         Page分页对象
     * @return IPage 返回结果
     */
    IPage<UserListView> page(UserListView userListView, Page<UserListView> page);


}
