package com.github.pettyfer.caas.framework.resource.controller;

import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.UserDetails;
import com.github.pettyfer.caas.global.userdetails.BaseUserDetails;
import com.github.pettyfer.caas.utils.SecurityUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@Api(value = "资源信息", tags = {"资源信息接口"})
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/resource")
public class ResourceController {

    @GetMapping("user-info")
    public UserDetails info() {
        BaseUserDetails user = SecurityUtil.getUser();
        return UserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .tenant(user.getTenant())
                .roles(user.getRoles())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .build();
    }

}
