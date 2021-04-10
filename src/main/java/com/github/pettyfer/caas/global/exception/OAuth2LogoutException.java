package com.github.pettyfer.caas.global.exception;

/**
 * @author Petty
 */
@SuppressWarnings("ALL")
public class OAuth2LogoutException extends RuntimeException {
    public OAuth2LogoutException(String message) {
        super(message);
    }
}
