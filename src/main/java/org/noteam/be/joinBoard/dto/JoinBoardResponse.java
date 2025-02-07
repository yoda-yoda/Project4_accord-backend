package org.noteam.be.joinBoard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.member.domain.Member;
import org.noteam.be.system.util.TimeAgoUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class JoinBoardResponse {

    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String topic;

    @NotNull
    private String teamName;

    @NotNull
    private String projectBio;

    @NotNull
    private String teamBio;

    @NotNull
    private String content;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull //
    private int peopleNumber;

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






    public static JoinBoardResponse fromEntity (JoinBoard joinBoard) {

       return JoinBoardResponse.builder()
                .id(joinBoard.getId())
                .title(joinBoard.getTitle())
                .topic(joinBoard.getTopic())
                .teamName(joinBoard.getTeamName())
                .projectBio(joinBoard.getProjectBio())
                .teamBio(joinBoard.getTeamBio())
                .content(joinBoard.getContent())
                .startDate(joinBoard.getStartDate())
                .endDate(joinBoard.getEndDate())
                .peopleNumber(joinBoard.getPeopleNumber())
                .createdAt( TimeAgoUtil.formatElapsedTime(joinBoard.getCreatedAt()) )
                .updatedAt( TimeAgoUtil.formatElapsedTime(joinBoard.getUpdatedAt()) )
                .memberId(joinBoard.getMember().getMemberId())
                .memberNickname(joinBoard.getMember().getNickname())
                .memberProfileUrl( joinBoard.getMember().getProfileImg() == null ? null : joinBoard.getMember().getProfileImg().getImageUrl() )
                .build();

    }



}
