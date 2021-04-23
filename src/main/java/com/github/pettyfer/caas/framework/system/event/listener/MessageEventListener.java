package com.github.pettyfer.caas.framework.system.event.listener;

import com.github.pettyfer.caas.framework.system.entity.SystemMessage;
import com.github.pettyfer.caas.framework.system.event.MessageEvent;
import com.github.pettyfer.caas.framework.system.service.ISystemMessageService;
import com.github.pettyfer.caas.global.model.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class MessageEventListener implements ApplicationListener<MessageEvent> {

    private final ISystemMessageService systemMessageService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageEventListener(ISystemMessageService systemMessageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.systemMessageService = systemMessageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onApplicationEvent(MessageEvent buildEvent) {
        SystemMessage message = buildEvent.getMessage();
        message.setCreator("robot");
        message.setCreateTime(LocalDateTime.now());
        systemMessageService.create(message);
        simpMessagingTemplate.convertAndSendToUser(buildEvent.getMessage().getReceiver(), "/topic/subscribe", new ServerMessage<>(message));
    }
}
