package com.github.pettyfer.caas.global.provider.token;

import com.github.pettyfer.caas.global.userdetails.BaseUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Pettyfer
 */
@Slf4j
public class BaseAccessTokenConverter extends DefaultAccessTokenConverter {

    public BaseAccessTokenConverter() {
        // 注入自定义用户认证转换器
        super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
    }

    public static class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
        @Override
        public Map<String, ?> convertUserAuthentication(Authentication authentication) {
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            BaseUserDetails principal = (BaseUserDetails) authentication.getPrincipal();
            response.put("id", principal.getId());
            response.put("username", principal.getUsername());
            response.put("status", principal.getStatus());
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
            response.put("tenant", principal.getTenant());
            return response;
        }
    }
}
