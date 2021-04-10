package com.github.pettyfer.caas.framework.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author admin
 */
public class PushEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private String message;

    public PushEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
