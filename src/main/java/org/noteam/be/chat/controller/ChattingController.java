package org.noteam.be.chat.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChattingController {

    @MessageMapping("/room/{roomId}") // 메세지가 발행(/publish 프리픽스 달리면)될 때 이쪽으로 온다.
    @SendTo("/topic/room/{roomId}") // 브로드캐스트해줄 구독경로.
    public String sendToRoom(@DestinationVariable String roomId, String message) {
        log.info("roomId = {}, message = {} ", roomId, message);
        return message; //@SendTo의 경로로 리턴함.
    }

}
