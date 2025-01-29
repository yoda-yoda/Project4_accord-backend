package org.noteam.be.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIMessageProcessor {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String CLIENT_ID = "13ca689b-2ae5-4b33-8c2c-1ec581fdfde3"; // 앨런 AI Client ID

    @RabbitListener(queues = "chat.queue")
    public void processChatMessage(String message) {
        try {
            fetchAllanAIResponseStreaming(message);
        } catch (Exception e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend("/topic/messages", "Error processing message.");
        }
    }

    private void fetchAllanAIResponseStreaming(String message) {
        String url = "https://kdt-api-function.azurewebsites.net/api/v1/question/sse-streaming?content="
                + message + "&client_id=" + CLIENT_ID;

        WebClient.create()
                .get()
                .uri(url)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(raw -> System.out.println("SSE raw line: " + raw))
                .concatMap(line -> parseAndStreamMessage(line)
                        .onErrorResume(e -> {
                            log.warn("Parsing error on chunk: {}, skipping", line, e);
                            return Mono.empty();
                        })
                )
                .subscribe();
    }

    private Flux<Void> parseAndStreamMessage(String rawData) {
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
                            messagingTemplate.convertAndSend(
                                    "/topic/messages",
                                    "{\"type\":\"continue\",\"text\":\"" + character + "\"}"
                            );
                            return Mono.empty();
                        });
            }
            else if ("complete".equals(type)) {
                log.info("Sending complete message = {}", content);
                messagingTemplate.convertAndSend(
                        "/topic/messages",
                        "{\"type\":\"complete\",\"text\":\"" + content + "\"}"
                );
            }
            return Flux.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }



}
