package org.noteam.be.chat.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Valid
@Builder
public class MessageRequest {

    //메시지 핵심 정보
    @NotBlank
    private String message;

    //보낸사람
    @NotBlank
    private Long senderId;

    //목적지(RoomId)
    private Long teamId;

}
