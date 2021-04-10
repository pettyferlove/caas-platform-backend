package com.github.pettyfer.caas.global.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2异常
 *
 * @author Petty
 */
@SuppressWarnings("ALL")
@JsonSerialize(using = BaseOAuth2ExceptionJacksonSerializer.class)
public class BaseOAuth2Exception extends OAuth2Exception {
    public BaseOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public BaseOAuth2Exception(String msg) {
        super(msg);
    }
}
