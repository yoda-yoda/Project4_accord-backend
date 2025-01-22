package org.noteam.be.joinBoard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.system.util.TimeAgoUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class JoinBoardCardResponse {

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
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private int peopleNumber;

    @NotNull
    private String createdAt;

    @NotNull
    private String updatedAt;

    public static JoinBoardCardResponse getResponseFromEntity(JoinBoard joinBoard){
       return JoinBoardCardResponse.builder()
                .id(joinBoard.getId())
                .title(joinBoard.getTitle())
                .topic(joinBoard.getTopic())
                .teamName(joinBoard.getTeamName())
                .projectBio(joinBoard.getProjectBio())
                .startDate(joinBoard.getStartDate())
                .endDate(joinBoard.getEndDate())
                .peopleNumber(joinBoard.getPeopleNumber())
                .createdAt(TimeAgoUtil.formatElapsedTime(joinBoard.getCreatedAt()))
                .updatedAt(TimeAgoUtil.formatElapsedTime(joinBoard.getUpdatedAt()))
                .build();
    }

    @Builder
    public JoinBoardCardResponse(Long id, String title, String topic, String teamName, String projectBio, LocalDate startDate, LocalDate endDate, int peopleNumber, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.teamName = teamName;
        this.projectBio = projectBio;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNumber = peopleNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
