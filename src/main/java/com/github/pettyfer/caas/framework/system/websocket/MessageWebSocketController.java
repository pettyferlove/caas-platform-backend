package com.github.pettyfer.caas.framework.system.websocket;

import com.github.pettyfer.caas.global.constants.MessageConstant;
import com.github.pettyfer.caas.global.model.ClientMessage;
import com.github.pettyfer.caas.global.model.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Petty
 */
@Slf4j
@RestController
public class MessageWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageWebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send")
    public void send(Principal principal, ClientMessage message) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                "/topic/subscribe", new ServerMessage<>(message.getMessage())
        );
    }

    @SubscribeMapping("/subscribe")
    public ServerMessage<String> subscribe(Principal principal) {
        return new ServerMessage<>(MessageConstant.WS_WELCOME, "welcome to use caas platform");
    }
}
