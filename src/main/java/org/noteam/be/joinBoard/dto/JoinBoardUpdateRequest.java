package org.noteam.be.joinBoard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class JoinBoardUpdateRequest {

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

    @Builder
    public JoinBoardUpdateRequest(String title, String topic, String teamName, String projectBio, String teamBio, String content, LocalDate startDate, LocalDate endDate, int peopleNumber) {
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
