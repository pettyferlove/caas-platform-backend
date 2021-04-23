package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.system.entity.SystemRole;
import com.github.pettyfer.caas.framework.system.mapper.SystemRoleMapper;
import com.github.pettyfer.caas.framework.system.service.ISystemRoleService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

    @Override
    public IPage<SystemRole> page(SystemRole systemRole, Page<SystemRole> page) {
        return this.page(page, Wrappers.lambdaQuery(systemRole).orderByDesc(SystemRole::getCreateTime));
    }

    @Override
    public SystemRole get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(SystemRole systemRole) {
        systemRole.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemRole.setCreateTime(LocalDateTime.now());
        if (this.save(systemRole)) {
            return systemRole.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(SystemRole systemRole) {
        systemRole.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemRole.setModifyTime(LocalDateTime.now());
        return this.updateById(systemRole);
    }

}
