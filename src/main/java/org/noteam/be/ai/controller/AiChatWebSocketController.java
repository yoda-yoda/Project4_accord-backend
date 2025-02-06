package org.noteam.be.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public void handleChatMessage(String message) {
        rabbitTemplate.convertAndSend("chat.queue", message);
    }


}