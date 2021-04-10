package com.github.pettyfer.caas.global.factory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.List;

/**
 * @author Petty
 */
public interface ITokenGranterFactory extends ApplicationContextAware, InitializingBean {

    /**
     * 获取TokenGranter列表
     * @return List
     */
    List<TokenGranter> getGranters();

}
