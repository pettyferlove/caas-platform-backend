package com.github.pettyfer.caas.framework.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
import com.github.pettyfer.caas.framework.core.service.INamespaceCoreService;
import com.github.pettyfer.caas.framework.system.entity.SystemUser;
import com.github.pettyfer.caas.framework.system.entity.SystemUserRole;
import com.github.pettyfer.caas.framework.system.mapper.SystemUserMapper;
import com.github.pettyfer.caas.framework.system.model.UserDetailsView;
import com.github.pettyfer.caas.framework.system.model.UserInitConfig;
import com.github.pettyfer.caas.framework.system.service.ISystemUserRoleService;
import com.github.pettyfer.caas.framework.system.service.ISystemUserService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ISystemUserRoleService userRoleService;

    private final IBizUserConfigurationService userConfigurationService;

    private final IBizGlobalConfigurationService globalConfigurationService;

    private final IBizNamespaceService namespaceService;

    private final INamespaceCoreService namespaceCoreService;

    private final PasswordEncoder passwordEncoder;

    public SystemUserServiceImpl(ISystemUserRoleService userRoleService, IBizUserConfigurationService userConfigurationService, IBizGlobalConfigurationService globalConfigurationService, IBizNamespaceService namespaceService, INamespaceCoreService namespaceCoreService, PasswordEncoder passwordEncoder) {
        this.userRoleService = userRoleService;
        this.userConfigurationService = userConfigurationService;
        this.globalConfigurationService = globalConfigurationService;
        this.namespaceService = namespaceService;
        this.namespaceCoreService = namespaceCoreService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public IPage<SystemUser> page(SystemUser systemUser, Page<SystemUser> page) {
        return this.page(page, Wrappers.lambdaQuery(systemUser).orderByDesc(SystemUser::getCreateTime));
    }

    @Override
    public UserDetailsView get(String id) {
        Optional<SystemUser> systemUserOptional = Optional.ofNullable(this.getById(id));
        if (systemUserOptional.isPresent()) {
            SystemUser systemUser = systemUserOptional.get();
            UserDetailsView detailsView = ConverterUtil.convert(systemUser, new UserDetailsView());
            detailsView.setPassword(null);
            LambdaQueryWrapper<SystemUserRole> queryWrapper = Wrappers.<SystemUserRole>lambdaQuery();
            queryWrapper.eq(SystemUserRole::getUserId, systemUser.getId());
            queryWrapper.eq(SystemUserRole::getDelFlag, 0);
            List<String> roleIds = userRoleService.list(queryWrapper).stream().map(SystemUserRole::getRoleId).collect(Collectors.toList());
            detailsView.setRoleIds(roleIds);
            return detailsView;
        } else {
            throw new BaseRuntimeException("用户不存在");
        }
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(UserDetailsView detailsView) {
        SystemUser systemUser = Optional.ofNullable(ConverterUtil.convert(detailsView, new SystemUser())).orElseGet(SystemUser::new);
        systemUser.setPassword(passwordEncoder.encode(detailsView.getPassword()));
        systemUser.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemUser.setCreateTime(LocalDateTime.now());
        if (this.save(systemUser)) {
            List<SystemUserRole> systemUserRoles = new ArrayList<>();
            List<String> roleIds = detailsView.getRoleIds();
            for (String roleId : roleIds) {
                if (StrUtil.isNotEmpty(roleId)) {
                    SystemUserRole systemUserRole = new SystemUserRole();
                    systemUserRole.setUserId(systemUser.getId());
                    systemUserRole.setRoleId(roleId);
                    systemUserRole.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
                    systemUserRole.setCreateTime(LocalDateTime.now());
                    systemUserRoles.add(systemUserRole);
                }
            }
            if (!systemUserRoles.isEmpty()) {
                userRoleService.saveBatch(systemUserRoles);
            }
            return systemUser.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean update(UserDetailsView detailsView) {
        SystemUser systemUser = Optional.ofNullable(ConverterUtil.convert(detailsView, new SystemUser())).orElseGet(SystemUser::new);
        systemUser.setPassword(null);
        if (StrUtil.isNotEmpty(systemUser.getId())) {
            systemUser.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
            systemUser.setModifyTime(LocalDateTime.now());
            LambdaQueryWrapper<SystemUserRole> queryWrapper = Wrappers.<SystemUserRole>lambdaQuery();
            queryWrapper.eq(SystemUserRole::getUserId, systemUser.getId());
            userRoleService.remove(queryWrapper);
            List<SystemUserRole> systemUserRoles = new ArrayList<>();
            List<String> roleIds = detailsView.getRoleIds();
            for (String roleId : roleIds) {
                if (StrUtil.isNotEmpty(roleId)) {
                    SystemUserRole systemUserRole = new SystemUserRole();
                    systemUserRole.setUserId(systemUser.getId());
                    systemUserRole.setRoleId(roleId);
                    systemUserRole.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
                    systemUserRole.setCreateTime(LocalDateTime.now());
                    systemUserRoles.add(systemUserRole);
                }
            }
            if (!systemUserRoles.isEmpty()) {
                userRoleService.saveBatch(systemUserRoles);
            }
            return this.updateById(systemUser);
        } else {
            throw new BaseRuntimeException("修改失败");
        }

    }

    @Override
    public Boolean checkConfig() {
        globalConfigurationService.checkConfiguration();
        userConfigurationService.checkConfiguration();
        namespaceService.checkNamespace();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean initConfig(UserInitConfig initConfig) {
        try {
            this.checkConfig();
        } catch (Exception e) {
            if (SecurityUtil.getRoles().contains("ADMIN")) {
                globalConfigurationService.create(initConfig.getGlobalConfiguration());
            }
            userConfigurationService.create(initConfig.getUserConfiguration());
            namespaceCoreService.create(initConfig.getNamespace());
        }
        return true;
    }

}
