package org.noteam.be.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.chat.dto.MessageRequest;
import org.noteam.be.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @JoinColumn(name="sender_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member senderMember;

    @Column(name="message")
    private String message;

    @Column(name ="team_id")
    public Long teamId;

    @Column(name = "send_at")
    private LocalDateTime sendAt = LocalDateTime.now();

    @Column(name= "status")
    private boolean status;


    @Builder
    public Message(Member senderMember, String message, Long teamId, LocalDateTime sendAt) {
        this.senderMember = senderMember;
        this.message = message;
        this.teamId = teamId;
        this.sendAt = sendAt;
        this.status = true;
    }

    public static Message of(MessageRequest msgReq, Member senderMember) {
        return Message.builder()
                .senderMember(senderMember) //이부분
                .teamId(msgReq.getTeamId())
                .message(msgReq.getMessage())
                .sendAt(LocalDateTime.now())
                .build();
    }


}
