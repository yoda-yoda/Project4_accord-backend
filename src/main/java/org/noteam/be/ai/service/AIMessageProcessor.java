package org.noteam.be.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.ai.dto.ChatMessageRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMessageProcessor {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String CLIENT_ID = "13ca689b-2ae5-4b33-8c2c-1ec581fdfde3";

    @RabbitListener(queues = "chat.queue")
    public void processChatMessage(String message) {
        try {
            ChatMessageRequest chatMessage = objectMapper.readValue(message, ChatMessageRequest.class);
            fetchAllanAIResponseStreaming(chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            String userId = getUserIdFromMessage(message);
            if (userId != null) {
                messagingTemplate.convertAndSend("/topic/messages." + userId, "Error processing message.");
            } else {
                log.error("Failed to extract userId from message: {}", message);
            }
        }
    }

    private void fetchAllanAIResponseStreaming(ChatMessageRequest chatMessage) {
        String url = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming?content="
                + chatMessage.getMessage() + "&client_id=" + CLIENT_ID;

        WebClient.create()
                .get()
                .uri(url)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .concatMap(line -> parseAndStreamMessage(line, chatMessage.getMemberId())
                        .onErrorResume(e -> {
                            log.warn("Parsing error on chunk: {}, skipping", line, e);
                            return Mono.empty();
                        })
                )
                .subscribe();
    }

    public Flux<Void> parseAndStreamMessage(String rawData, String userId) {
        try {
            String trimmed = rawData.trim();
            if (trimmed.startsWith("data:")) {
                trimmed = trimmed.substring("data:".length()).trim();
            }
            trimmed = trimmed.replace('\'', '"');

            JsonNode jsonNode = objectMapper.readTree(trimmed);

            String type = jsonNode.get("type").asText();
            String content = jsonNode.get("data").get("content").asText();

            if ("continue".equals(type)) {
                List<String> charList = content.chars()
                        .mapToObj(c -> String.valueOf((char) c))
                        .collect(Collectors.toList());

                return Flux.fromIterable(charList)
                        .delayElements(Duration.ofMillis(20))
                        .concatMap(character -> {
                            sendChunk(userId, "continue", character);
                            return Mono.empty();
                        });

            } else if ("complete".equals(type)) {
                sendChunk(userId, "complete", content);
            }
            return Flux.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }

    private String getUserIdFromMessage(String message) {
        try {
            ChatMessageRequest chatMessage = objectMapper.readValue(message, ChatMessageRequest.class);
            return chatMessage.getMemberId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendChunk(String userId, String type, String content) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("type", type);
        msgMap.put("text", content);

        try {
            String jsonString = objectMapper.writeValueAsString(msgMap);
            messagingTemplate.convertAndSend("/topic/messages." + userId, jsonString);
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 에러: {}", e.getMessage(), e);
        }
    }


}
