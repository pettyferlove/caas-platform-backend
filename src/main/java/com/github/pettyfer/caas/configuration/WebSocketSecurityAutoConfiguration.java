package com.github.pettyfer.caas.configuration;

import com.github.pettyfer.caas.global.interceptor.UserChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


/**
 * @author Petty
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityAutoConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    /**
     * 注册stomp的端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/v1/message/ws").setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor());
        super.customizeClientInboundChannel(registration);
    }

    /**
     * 配置消息代理(message broker)
     *
     * @param registry MessageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 订阅Broker名称
        registry.enableSimpleBroker("/queue", "/topic");
        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
        registry.setApplicationDestinationPrefixes("/app");
        // 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        registry.setUserDestinationPrefix("/user/");
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Bean
    public UserChannelInterceptor channelInterceptor() {
        return new UserChannelInterceptor();
    }
}
