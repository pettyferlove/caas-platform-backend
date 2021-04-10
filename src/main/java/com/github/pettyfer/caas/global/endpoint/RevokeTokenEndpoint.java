package com.github.pettyfer.caas.global.endpoint;

import cn.hutool.core.util.StrUtil;
import com.github.pettyfer.caas.global.exception.OAuth2LogoutException;
import com.github.pettyfer.caas.global.model.Logout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Petty
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    private final TokenStore tokenStore;

    @Autowired
    public RevokeTokenEndpoint(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public ResponseEntity<Logout> revokeToken(String token) {
        if (StringUtils.hasText(token)) {
            try {
                OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
                if (StrUtil.isBlank(accessToken.getValue())) {
                    throw new OAuth2LogoutException("logout fail");
                }
                tokenStore.removeAccessToken(accessToken);
                return ResponseEntity.ok(new Logout("logout success", System.currentTimeMillis(), 200));
            } catch (Exception e) {
                return ResponseEntity.ok(new Logout("token already remove", System.currentTimeMillis(), 200));
            }
        } else {
            return ResponseEntity.status(204).body(new Logout("no token", System.currentTimeMillis(), 200));
        }
    }
}
