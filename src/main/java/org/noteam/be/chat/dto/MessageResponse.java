package org.noteam.be.chat.dto;


import lombok.Builder;
import lombok.Data;
import org.noteam.be.chat.domain.Message;
import org.noteam.be.system.util.TimeAgoUtil;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {

    //메시지 정보
    private Long messageId;
    private String message;
    private String sendDateTime;

    //메시지 보낸사람 정보
    private Long senderId;
    private String senderName;
    private String senderProfileImageUrl;

    //보낼곳
    private Long teamId;

    public static MessageResponse from(Message message) {
        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .message(message.getMessage())
                .sendDateTime(TimeAgoUtil.formatElapsedTime(message.getSendAt() == null ? LocalDateTime.now() : message.getSendAt()))
                .senderId(message.getSenderMember().getMemberId())
                .senderProfileImageUrl(message.getSenderMember().getProfileImg() == null ? null : message.getSenderMember().getProfileImg().getImageUrl())
                .senderName(message.getSenderMember().getNickname())
                .teamId(message.getTeamId())
                .build();
    }

}
