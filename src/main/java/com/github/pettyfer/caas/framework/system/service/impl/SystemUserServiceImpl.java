package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.system.entity.SystemUser;
import com.github.pettyfer.caas.framework.system.mapper.SystemUserMapper;
import com.github.pettyfer.caas.framework.system.service.ISystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    @Override
    public IPage<SystemUser> page(SystemUser systemUser, Page<SystemUser> page) {
        return this.page(page, Wrappers.lambdaQuery(systemUser).orderByDesc(SystemUser::getCreateTime));
    }

    @Override
    public SystemUser get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(SystemUser systemUser) {
        systemUser.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemUser.setCreateTime(LocalDateTime.now());
        if (this.save(systemUser)) {
            return systemUser.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(SystemUser systemUser) {
        systemUser.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemUser.setModifyTime(LocalDateTime.now());
        return this.updateById(systemUser);
    }

}
