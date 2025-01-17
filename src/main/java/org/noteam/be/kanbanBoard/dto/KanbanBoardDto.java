package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.noteam.be.team.domain.Team;


@Data
@AllArgsConstructor
public class KanbanBoardDto {

    private Team team;

    private String title;
}
