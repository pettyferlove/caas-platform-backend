package com.github.pettyfer.caas.framework.system.event;

import com.github.pettyfer.caas.framework.system.entity.SystemMessage;
import org.springframework.context.ApplicationEvent;

/**
 * @author admin
 */
public class MessageEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private SystemMessage message;

    public MessageEvent(Object source, SystemMessage message) {
        super(source);
        this.message = message;
    }

    public SystemMessage getMessage() {
        return message;
    }

    public void setMessage(SystemMessage message) {
        this.message = message;
    }
}
