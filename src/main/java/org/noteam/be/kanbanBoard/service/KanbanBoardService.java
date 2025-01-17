package org.noteam.be.kanbanBoard.service;

import org.noteam.be.team.domain.Team;

public interface KanbanBoardService{
    void createBoard(Team team, String title);
}
