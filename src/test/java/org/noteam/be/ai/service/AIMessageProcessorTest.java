package org.noteam.be.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class AIMessageProcessorTest {

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    AIMessageProcessor aiMessageProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testParseAndStreamMessage_continueType() {
        // given
        String sseData = "data: {\"type\":\"continue\",\"data\":{\"content\":\"Hello\"}}";
        String userId = "user-123";

        // when
        Flux<Void> flux = aiMessageProcessor.parseAndStreamMessage(sseData, userId);

        // then
        StepVerifier.create(flux)
                .verifyComplete();

        Mockito.verify(messagingTemplate, Mockito.times(5))
                .convertAndSend(
                        ArgumentMatchers.eq("/topic/messages." + userId),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    void testParseAndStreamMessage_completeType() {
        // given
        String sseData = "data: {\"type\":\"complete\",\"data\":{\"content\":\"Done!\"}}";
        String userId = "user-123";

        // when
        Flux<Void> flux = aiMessageProcessor.parseAndStreamMessage(sseData, userId);

        // then
        StepVerifier.create(flux)
                .verifyComplete();

        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSend(
                        ArgumentMatchers.eq("/topic/messages." + userId),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    void testParseAndStreamMessage_invalidJson() {
        // given
        String sseData = "data: {\"type\":\"unknown\"}";
        String userId = "user-123";

        // when
        Flux<Void> flux = aiMessageProcessor.parseAndStreamMessage(sseData, userId);

        // then
        StepVerifier.create(flux)
                .verifyComplete();

        Mockito.verifyNoInteractions(messagingTemplate);
    }

    @Test
    void testSendChunk() throws JsonProcessingException {
        String userId = "user-123";
        String type = "continue";
        String content = "H";

        aiMessageProcessor.sendChunk(userId, type, content);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(messagingTemplate).convertAndSend(
                Mockito.eq("/topic/messages." + userId),
                captor.capture()
        );

        String sentJson = captor.getValue();
        JsonNode node = new ObjectMapper().readTree(sentJson);
        assert node.get("type").asText().equals("continue");
        assert node.get("text").asText().equals("H");
    }

}