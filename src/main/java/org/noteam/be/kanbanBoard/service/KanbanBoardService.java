package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.team.domain.Team;

import java.util.List;

public interface KanbanBoardService{
    void createBoard(Team team, String title);

    List<KanbanBoard> getAllTeamBoards(Long teamId);

    List<KanbanBoard> getKanbanBoardList(Long id);

    List<KanbanBoard> getBoardList(Long teamId);

    KanbanBoard findKanbanBoardbyTeamIdAndTitle(Long teamId, String title);

    KanbanBoard createBoard(Long memberId, String title);
}
