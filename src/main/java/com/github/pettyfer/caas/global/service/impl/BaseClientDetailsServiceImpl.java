package com.github.pettyfer.caas.global.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pettyfer.caas.framework.system.entity.SystemOauthClientDetails;
import com.github.pettyfer.caas.framework.system.service.ISystemOauthClientDetailsService;
import com.github.pettyfer.caas.global.constants.EncryptionConstant;
import com.github.pettyfer.caas.global.service.OAuth2ClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Petty
 */
@Slf4j
@Service
public class BaseClientDetailsServiceImpl implements OAuth2ClientDetailsService {

    private final ISystemOauthClientDetailsService oauthClientDetailsService;

    public BaseClientDetailsServiceImpl(ISystemOauthClientDetailsService oauthClientDetailsService) {
        this.oauthClientDetailsService = oauthClientDetailsService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        SystemOauthClientDetails clientDetails = oauthClientDetailsService.getOne(Wrappers.<SystemOauthClientDetails>lambdaQuery().eq(SystemOauthClientDetails::getClientId, clientId).eq(SystemOauthClientDetails::getDelFlag, false));
        BaseClientDetails baseClientDetails = new BaseClientDetails(clientDetails.getClientId(), clientDetails.getResourceIds(),
                clientDetails.getScope(), clientDetails.getAuthorizedGrantTypes(), clientDetails.getAuthorities(), clientDetails.getWebServerRedirectUri());
        baseClientDetails.setClientSecret(EncryptionConstant.SIGNATURE_NOOP + clientDetails.getClientSecret());
        if (clientDetails.getAccessTokenValidity() != null) {
            baseClientDetails.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValidity());
        }
        if (clientDetails.getRefreshTokenValidity() != null) {
            baseClientDetails.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValidity());
        }


        String json = clientDetails.getAdditionalInformation();
        if (json != null) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> additionalInformation = JSON.parseObject(json).getInnerMap();
                baseClientDetails.setAdditionalInformation(additionalInformation);
            } catch (Exception e) {
                log.warn("Could not decode JSON for additional information: " + baseClientDetails, e);
            }
        }
        String scopes = clientDetails.getAutoApprove();
        if (scopes != null) {
            baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
        }

        return baseClientDetails;
    }
}
