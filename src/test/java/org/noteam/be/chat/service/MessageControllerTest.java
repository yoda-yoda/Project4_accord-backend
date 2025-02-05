//package org.noteam.be.chat.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.noteam.be.chat.dto.MessageRequest;
//import org.noteam.be.chat.dto.MessageResponse;
//
//import static org.mockito.Mockito.when;
//
//@MockitoSettings
//class MessageControllerTest {
//
//
//    @Mock
//    private MessageService messageService;
//
//    MessageRequest mockRequest = MessageRequest.builder()
//            .message("Test Message")
//            .senderId(101L)
//            .connectType("1:N")
//            .destinationId("room-1")
//            .build();
//
//    MessageResponse mockResponse = MessageResponse.builder()
//            .messageId(1L)
//            .message("Test Message")
//            .sendDateTime("2025-01-24T12:00:00")
//            .senderId(101L)
//            .senderName("Tester")
//            .connectType("1:N")
//            .destinationId("room-1")
//            .build();
//
//    @Test
//    @DisplayName("SendMessage Test")
//    void sendMessageTest() throws Exception{
//        // given
//        // 행동 정의
//        when(messageService.sendMessage(mockRequest)).thenReturn(mockResponse);
//        // when
//
//
//        // then
//    }
//
//}