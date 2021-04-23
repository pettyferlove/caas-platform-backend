package com.github.pettyfer.caas.global.userdetails;

import cn.hutool.core.util.StrUtil;
import com.github.pettyfer.caas.global.constants.SecurityConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author Petty
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserDetails implements UserDetails {

    private static final long serialVersionUID = 1254385573535663771L;

    /**
     * 用户ID
     */
    private String id;

    private String username;

    private String avatar;

    private String password;

    private Integer status;

    private String nickname;

    private String tenantId;
    /**
     * 所属租户ID
     */
    private String tenant;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        for (String role : roles) {
            if (StrUtil.isNotEmpty(role)) {
                String r = role.toUpperCase();
                if (!r.contains(SecurityConstant.ROLE_PREFIX)) {
                    authoritySet.add(new SimpleGrantedAuthority(SecurityConstant.ROLE_PREFIX + r));
                } else {
                    authoritySet.add(new SimpleGrantedAuthority(r));
                }
            }
        }
        authoritySet.add(new SimpleGrantedAuthority(SecurityConstant.ROLE_PREFIX + SecurityConstant.BASE_ROLE));
        return authoritySet;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !Objects.equals(SecurityConstant.STATUS_INVALID, status);
    }

    /**
     * 用户是否锁定
     *
     * @return True 表示锁定，False 表示未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return !Objects.equals(SecurityConstant.STATUS_LOCK, status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !Objects.equals(SecurityConstant.STATUS_CREDENTIALS_EXPIRED, status);
    }

    /**
     * 用户是否有效
     *
     * @return True有效，False无效
     */
    @Override
    public boolean isEnabled() {
        return Objects.equals(SecurityConstant.STATUS_NORMAL, status);
    }
}
