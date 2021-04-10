package com.github.pettyfer.caas.utils;


import cn.hutool.core.util.StrUtil;
import com.github.pettyfer.caas.global.constants.SecurityConstant;
import com.github.pettyfer.caas.global.userdetails.BaseUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author Petty
 */
@UtilityClass
public class SecurityUtil {

    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    private BaseUserDetails getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof BaseUserDetails) {
            return (BaseUserDetails) principal;
        }
        return null;
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public OAuth2Authentication getOAuth2Authentication() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            return (OAuth2Authentication) authentication;
        }
        return null;
    }

    public String getClientId() {
        return getOAuth2Authentication().getOAuth2Request().getClientId();
    }

    /**
     * 获取用户
     */
    public BaseUserDetails getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<String> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<>();
        authorities.stream()
                .filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstant.ROLE_PREFIX))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstant.ROLE_PREFIX);
                    roles.add(id);
                });
        return roles;
    }
}
