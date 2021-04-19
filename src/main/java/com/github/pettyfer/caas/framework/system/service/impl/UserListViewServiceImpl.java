package com.github.pettyfer.caas.framework.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.system.entity.UserListView;
import com.github.pettyfer.caas.framework.system.mapper.UserListViewMapper;
import com.github.pettyfer.caas.framework.system.service.IUserListViewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-16
 */
@Service
public class UserListViewServiceImpl extends ServiceImpl<UserListViewMapper, UserListView> implements IUserListViewService {

    @Override
    public IPage<UserListView> page(UserListView userListView, Page<UserListView> page) {
        LambdaQueryWrapper<UserListView> queryWrapper = Wrappers.lambdaQuery(userListView).orderByDesc(UserListView::getCreateTime);
        queryWrapper.likeRight(StrUtil.isNotEmpty(userListView.getUserName()), UserListView::getUserName, userListView.getUserName());
        queryWrapper.like(StrUtil.isNotEmpty(userListView.getRoles()), UserListView::getRoles, userListView.getRoles());
        return this.page(page, queryWrapper);
    }
}
