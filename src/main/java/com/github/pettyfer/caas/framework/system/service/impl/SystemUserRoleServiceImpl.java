package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.system.entity.SystemUserRole;
import com.github.pettyfer.caas.framework.system.mapper.SystemUserRoleMapper;
import com.github.pettyfer.caas.framework.system.service.ISystemUserRoleService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户角色关联信息 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Service
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements ISystemUserRoleService {

    @Override
    public IPage<SystemUserRole> page(SystemUserRole systemUserRole, Page<SystemUserRole> page) {
        return this.page(page, Wrappers.lambdaQuery(systemUserRole).orderByDesc(SystemUserRole::getCreateTime));
    }

    @Override
    public SystemUserRole get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(SystemUserRole systemUserRole) {
        systemUserRole.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemUserRole.setCreateTime(LocalDateTime.now());
        if (this.save(systemUserRole)) {
            return systemUserRole.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(SystemUserRole systemUserRole) {
        systemUserRole.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemUserRole.setModifyTime(LocalDateTime.now());
        return this.updateById(systemUserRole);
    }

}
