package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.dto.*;

import java.util.List;

public interface KanbanBoardService{


    List<KanbanBoard> getAllTeamBoards(Long teamId);

    List<KanbanBoard> getKanbanBoardList(Long id);

    List<KanbanBoard> getBoardList(Long teamId);

    KanbanBoard getKanbanBoardbyTeamIdAndTitle(KanbanBoardLookupRequest request );

    KanbanBoard getKanbanBoardbyBoardId(Long boardId);

    KanbanBoardMessageResponse createBoard(KanbanBoardCreateRequest request);

    KanbanBoardMessageResponse deleteBoard(Long boardId);

    KanbanBoardMessageResponse updateBoard(KanbanBoardUpdateRequest request);

    KanbanBoardMessageResponse changeBoardPriority(KanbanBoardSwitchRequest request);

    KanbanBoard getKanbanBoardById(Long currentBoardId);
}
