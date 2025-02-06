package org.noteam.be.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.*;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost") // localhost
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/publish", "/app");  
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // 클라이언트가 웹소켓 STOMP 에 연결할 엔드포인트
                .setAllowedOriginPatterns("*"); // Cors 허용

       registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000");
    }

}
