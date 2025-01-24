package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;
import org.noteam.be.member.domain.Member;

import java.util.List;

public interface KanbanBoardCardService {


    KanbanBoardCard create(String content, Member member, KanbanBoard board, Long num);

    //Card 조회 로직
    KanbanBoardCard getKanbanBoardCard(Long id);

    List<KanbanBoardCard> getKanbanBoardCardbyBoardId(Long boardId);

    KanbanBoardMessageResponse createCard(Long memberId, Long teamId , String title, String content);

    KanbanBoardMessageResponse deleteBoardCard(Long boardId);

    KanbanBoardMessageResponse updateCard(Long id, String content);

    KanbanBoardMessageResponse changeCardPriority(Long cardId, Long boardId, int dropSpotNum);

    String forEachCard(List<KanbanBoardCard> cardList);
}
