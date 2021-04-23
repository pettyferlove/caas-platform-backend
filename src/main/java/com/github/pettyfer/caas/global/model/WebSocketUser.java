package com.github.pettyfer.caas.global.model;

import java.security.Principal;

/**
 * 重写Principal，用User UUID作为WebSocket认证过程中的标识符
 *
 * @author Petty
 */
public class WebSocketUser implements Principal {

    private final String userId;

    public WebSocketUser(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return this.userId;
    }
}
