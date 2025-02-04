package org.noteam.be.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AiChatWebSocketController {

    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String handleChatMessage(String message) {
        rabbitTemplate.convertAndSend("chat.queue", message);
        return "AI 응답을 기다리는 중...";
    }


}
