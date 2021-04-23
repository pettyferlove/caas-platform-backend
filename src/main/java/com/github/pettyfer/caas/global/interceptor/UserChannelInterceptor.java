package com.github.pettyfer.caas.global.interceptor;

import com.github.pettyfer.caas.global.model.WebSocketUser;
import com.github.pettyfer.caas.global.userdetails.BaseUserDetails;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Petty
 */
public class UserChannelInterceptor implements ChannelInterceptor {


    /**
     * 将鉴权用户转换为WebSocket用户，用户名对应UserID
     *
     * @param message Message
     * @param channel MessageChannel
     * @return Message
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            OAuth2Authentication authentication = (OAuth2Authentication) accessor.getHeader(StompHeaderAccessor.USER_HEADER);
            assert authentication != null;
            BaseUserDetails userDetails = (BaseUserDetails) authentication.getUserAuthentication().getPrincipal();
            WebSocketUser user = new WebSocketUser(userDetails.getId());
            accessor.setUser(user);
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return null;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

    }
}
