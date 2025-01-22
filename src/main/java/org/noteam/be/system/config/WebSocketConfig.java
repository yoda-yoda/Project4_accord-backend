package org.noteam.be.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //심플메세지 브로커설정, 레빗엠큐에서는 쓰지 않음.
        //config.enableSimpleBroker("/subscribe");

        //외부 브로커 설정하기.
        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost") // localhost
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/publish");  // 프리픽스 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // 클라이언트가 웹소켓 STOMP 에 연결할 엔드포인트
                .setAllowedOrigins("*"); // Cors 허용
//                .withSockJS(); // SockJS 사용 설정
    }

}
