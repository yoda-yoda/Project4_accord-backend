package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardCardCreateRequest;
import org.noteam.be.kanbanBoard.dto.KanbanBoardCardSwitchRequest;
import org.noteam.be.kanbanBoard.dto.KanbanBoardCardUpdateRequest;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;
import org.noteam.be.member.domain.Member;

import java.util.List;

public interface KanbanBoardCardService {


    KanbanBoardCard create(String content, Member member, KanbanBoard board, Long num);

    //Card 조회 로직
    KanbanBoardCard getKanbanBoardCard(Long id);

    List<KanbanBoardCard> getKanbanBoardCardbyBoardId(Long boardId);

    KanbanBoardMessageResponse createCard(KanbanBoardCardCreateRequest request);

    KanbanBoardMessageResponse deleteBoardCard(Long boardId);

    KanbanBoardMessageResponse updateCard(KanbanBoardCardUpdateRequest request);

    public KanbanBoardMessageResponse changeCardPriority(KanbanBoardCardSwitchRequest request);

    String forEachCard(List<KanbanBoardCard> cardList);
}
