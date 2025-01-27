package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;

import java.util.List;

public interface KanbanBoardService{


    List<KanbanBoard> getAllTeamBoards(Long teamId);

    List<KanbanBoard> getKanbanBoardList(Long id);

    List<KanbanBoard> getBoardList(Long teamId);

    KanbanBoard getKanbanBoardbyTeamIdAndTitle(Long teamId, String title);

    KanbanBoard getKanbanBoardbyBoardId(Long boardId);

    KanbanBoardMessageResponse createBoard(Long memberId, String title);

    KanbanBoardMessageResponse deleteBoard(Long boardId);

    KanbanBoardMessageResponse updateBoard(Long id, String title);

    KanbanBoardMessageResponse changeBoardPriority(Long boardId, int toId, Long teamId);
}
