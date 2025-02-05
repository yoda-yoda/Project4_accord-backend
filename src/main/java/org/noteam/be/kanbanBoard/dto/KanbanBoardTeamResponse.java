package org.noteam.be.kanbanBoard.dto;

import lombok.Builder;
import lombok.Data;
import org.noteam.be.team.domain.Team;


@Data
@Builder
public class KanbanBoardTeamResponse {
    private String teamName;
}



