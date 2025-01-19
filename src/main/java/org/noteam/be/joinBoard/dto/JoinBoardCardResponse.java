package org.noteam.be.joinBoard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class JoinBoardCardResponse {

    private Long id;

    private String title;

    private String topic;

    private String teamName;

    private String projectBio;

    private LocalDate startDate;

    private LocalDate endDate;

    private int peopleNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
                .createdAt(joinBoard.getCreatedAt())
                .updatedAt(joinBoard.getUpdatedAt())
                .build();
    }

    @Builder
    public JoinBoardCardResponse(Long id, String title, String topic, String teamName, String projectBio, LocalDate startDate, LocalDate endDate, int peopleNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
