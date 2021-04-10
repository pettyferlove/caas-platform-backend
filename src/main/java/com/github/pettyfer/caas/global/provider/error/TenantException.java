package com.github.pettyfer.caas.global.provider.error;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Petty
 */
public class TenantException extends AuthenticationException {
    // ~ Constructors
    // ===================================================================================================

    /**
     * Constructs a <code>UsernameNotFoundException</code> with the specified message.
     *
     * @param msg the detail message.
     */
    public TenantException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code UsernameNotFoundException} with the specified message and root
     * cause.
     *
     * @param msg the detail message.
     * @param t   root cause
     */
    public TenantException(String msg, Throwable t) {
        super(msg, t);
    }
}
