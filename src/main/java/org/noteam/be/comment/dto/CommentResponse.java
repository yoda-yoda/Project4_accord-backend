package org.noteam.be.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.system.util.TimeAgoUtil;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    @NotNull
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private String createdAt;

    @NotNull
    private String updatedAt;


    @NotNull
    private Long memberId;

    @NotNull
    private String memberNickname;

    @NotNull
    private String memberProfileUrl;




    public static CommentResponse fromEntity (Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt( TimeAgoUtil.formatElapsedTime(comment.getCreatedAt()) )
                .updatedAt( TimeAgoUtil.formatElapsedTime(comment.getUpdatedAt()) )
                .memberId( comment.getMember().getMemberId() )
                .memberNickname( comment.getMember().getNickname() )
                .memberProfileUrl( comment.getMember().getProfileImg() == null ? null : comment.getMember().getProfileImg().getImageUrl() )
                .build();
    }



}
