package com.github.pettyfer.caas.global.factory.impl;

import com.github.pettyfer.caas.global.factory.ITokenGranterFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Petty
 */
public class TokenGranterFactoryImpl implements ITokenGranterFactory {

    private Map<String, TokenGranter> serviceMap;

    private ApplicationContext applicationContext;

    @Override
    public List<TokenGranter> getGranters() {
        return new ArrayList<>(serviceMap.values());
    }

    @Override
    public void afterPropertiesSet() {
        this.serviceMap = this.applicationContext.getBeansOfType(TokenGranter.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
