package org.noteam.be.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.member.domain.Member;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentRegisterRequest {

    @NotNull
    private String content;


    public static Comment toEntity(CommentRegisterRequest dto, Member member, JoinBoard joinBoard) {
        return Comment.builder()
                .content(dto.getContent())
                .member(member)
                .joinBoard(joinBoard)
                .build();
    }

}
