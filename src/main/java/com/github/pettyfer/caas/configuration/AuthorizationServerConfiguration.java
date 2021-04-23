package com.github.pettyfer.caas.configuration;

import com.github.pettyfer.caas.global.factory.ITokenGranterFactory;
import com.github.pettyfer.caas.global.factory.impl.TokenGranterFactoryImpl;
import com.github.pettyfer.caas.global.filter.BaseClientCredentialsTokenEndpointFilter;
import com.github.pettyfer.caas.global.provider.error.OAuth2AuthExceptionEntryPoint;
import com.github.pettyfer.caas.global.provider.error.ResponseExceptionTranslator;
import com.github.pettyfer.caas.global.provider.token.BaseAccessTokenConverter;
import com.github.pettyfer.caas.global.service.OAuth2ClientDetailsService;
import com.github.pettyfer.caas.global.service.OAuth2UserDetailsService;
import com.github.pettyfer.caas.utils.KeyUtil;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.*;

/**
 * @author Petty
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;

    private final OAuth2ClientDetailsService clientDetailsService;

    private final OAuth2UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ITokenGranterFactory tokenGranterFactory() {
        return new TokenGranterFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2RequestFactory.class)
    public OAuth2RequestFactory requestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenEnhancer(customerEnhancer());
        return defaultTokenServices;
    }

    public AuthorizationServerConfiguration(TokenStore tokenStore, OAuth2ClientDetailsService clientDetailsService, OAuth2UserDetailsService userDetailsService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.tokenStore = tokenStore;
        this.clientDetailsService = clientDetailsService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        OAuth2AuthExceptionEntryPoint entryPoint = new OAuth2AuthExceptionEntryPoint();
        security.passwordEncoder(passwordEncoder);
        security.authenticationEntryPoint(entryPoint);
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        BaseClientCredentialsTokenEndpointFilter endpoint = new BaseClientCredentialsTokenEndpointFilter(security);
        endpoint.afterPropertiesSet();
        endpoint.setAuthenticationEntryPoint(entryPoint);
        security.addTokenEndpointAuthenticationFilter(endpoint);
    }

    /**
     * 将自定义Granter加入至授权池中
     *
     * @param authorizationCodeServices 默认初始化的AuthorizationCodeServices，不要自己初始化，会导致会在两个Service，直接导致AuthorizationCode模式失效
     * @return List
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationCodeServices authorizationCodeServices) {
        ClientDetailsService clientDetails = clientDetailsService;
        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices(),
                authorizationCodeServices, clientDetails, requestFactory()));
        tokenGranters.add(new RefreshTokenGranter(tokenServices(), clientDetails, requestFactory()));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices(), clientDetails,
                requestFactory());
        tokenGranters.add(implicit);
        tokenGranters.add(
                new ClientCredentialsTokenGranter(tokenServices(), clientDetails, requestFactory()));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager,
                    tokenServices(), clientDetails, requestFactory()));
        }
        List<TokenGranter> granters = tokenGranterFactory().getGranters();
        tokenGranters.addAll(granters);
        return tokenGranters;
    }

    private TokenGranter tokenGranter(AuthorizationCodeServices authorizationCodeServices) {
        return new TokenGranter() {
            private CompositeTokenGranter delegate;

            @Override
            public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                if (delegate == null) {
                    delegate = new CompositeTokenGranter(getDefaultTokenGranters(authorizationCodeServices));
                }
                return delegate.grant(grantType, tokenRequest);
            }
        };
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //Token存储位置
        endpoints.tokenStore(tokenStore);
        endpoints.authenticationManager(authenticationManager);
        endpoints.approvalStore(approvalStore());
        endpoints.userDetailsService(userDetailsService);
        endpoints.exceptionTranslator(new ResponseExceptionTranslator());
        endpoints.tokenGranter(tokenGranter(endpoints.getAuthorizationCodeServices()));
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        super.configure(endpoints);
    }

    @Bean
    public TokenEnhancer customerEnhancer() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
        return tokenEnhancerChain;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final RsaSigner signer = new RsaSigner(KeyUtil.getSignerKey());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            private final JsonParser objectMapper = JsonParserFactory.create();

            @Override
            protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String content;
                try {
                    content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
                } catch (Exception ex) {
                    throw new IllegalStateException("Cannot convert access token to JSON", ex);
                }
                Map<String, String> headers = new HashMap<>();
                headers.put("kid", KeyUtil.VERIFIER_KEY_ID);
                return JwtHelper.encode(content, signer, headers).getEncoded();
            }
        };
        converter.setSigner(signer);
        converter.setVerifier(new RsaVerifier(KeyUtil.getVerifierKey()));
        converter.setAccessTokenConverter(new BaseAccessTokenConverter());
        return converter;
    }

    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder(KeyUtil.getVerifierKey())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(KeyUtil.VERIFIER_KEY_ID);
        return new JWKSet(builder.build());
    }

}
