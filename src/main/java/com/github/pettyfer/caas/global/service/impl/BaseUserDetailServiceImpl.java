package com.github.pettyfer.caas.global.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pettyfer.caas.framework.system.entity.SystemRole;
import com.github.pettyfer.caas.framework.system.entity.SystemUser;
import com.github.pettyfer.caas.framework.system.entity.SystemUserRole;
import com.github.pettyfer.caas.framework.system.service.ISystemRoleService;
import com.github.pettyfer.caas.framework.system.service.ISystemUserRoleService;
import com.github.pettyfer.caas.framework.system.service.ISystemUserService;
import com.github.pettyfer.caas.global.service.OAuth2UserDetailsService;
import com.github.pettyfer.caas.global.userdetails.BaseUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Petty
 */
@Slf4j
@Service
public class BaseUserDetailServiceImpl implements OAuth2UserDetailsService {

    private final ISystemUserService userService;

    private final ISystemRoleService roleService;

    private final ISystemUserRoleService userRoleService;

    public BaseUserDetailServiceImpl(ISystemUserService userService, ISystemRoleService roleService, ISystemUserRoleService userRoleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SystemUser> systemUserOptional = Optional.ofNullable(userService.getOne(Wrappers.<SystemUser>lambdaQuery().eq(SystemUser::getLoginName, username)));
        if (systemUserOptional.isPresent()) {
            SystemUser systemUser = systemUserOptional.get();
            List<SystemUserRole> systemUserRoles = Optional.ofNullable(userRoleService.list(Wrappers.<SystemUserRole>lambdaQuery().eq(SystemUserRole::getUserId, systemUser.getId()))).orElseGet(ArrayList::new);
            List<String> roleIds = systemUserRoles.stream().map(SystemUserRole::getRoleId).collect(Collectors.toList());
            List<String> roles = new LinkedList<>();
            if (!roleIds.isEmpty()) {
                List<SystemRole> roleList = Optional.ofNullable(roleService.list(Wrappers.<SystemRole>lambdaQuery().in(SystemRole::getId, roleIds))).orElseGet(LinkedList::new);
                roles = roleList.stream().map(SystemRole::getRole).collect(Collectors.toList());
            }
            return BaseUserDetails.builder()
                    .id(systemUser.getId())
                    .username(systemUser.getLoginName())
                    .password(systemUser.getPassword())
                    .status(systemUser.getStatus())
                    .nickname(systemUser.getUserName())
                    .roles(roles)
                    .tenant(systemUser.getTenantId())
                    .avatar(systemUser.getUserAvatar())
                    .build();
        } else {
            throw new UsernameNotFoundException("用户未注册");
        }
    }
}
