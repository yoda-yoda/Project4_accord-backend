package org.noteam.be.joinBoard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.noteam.be.joinBoard.domain.JoinBoard;
import java.time.LocalDate;

@Getter
public class JoinBoardRegisterRequest {

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

    @NotNull
    private int peopleNumber;


    public static JoinBoard toEntity(JoinBoardRegisterRequest dto) {
        return JoinBoard.builder()
                .title(dto.getTitle())
                .topic(dto.getTopic())
                .teamName(dto.getTeamName())
                .projectBio(dto.getProjectBio())
                .teamBio(dto.getTeamBio())
                .content(dto.getContent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .peopleNumber(dto.getPeopleNumber())
                .build();
    }


    @Builder
    public JoinBoardRegisterRequest(String title, String topic, String teamName, String projectBio, String teamBio, String content, LocalDate startDate, LocalDate endDate, int peopleNumber) {
        this.title = title;
        this.topic = topic;
        this.teamName = teamName;
        this.projectBio = projectBio;
        this.teamBio = teamBio;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNumber = peopleNumber;
    }
}
