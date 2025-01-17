package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.member.domain.Member;

public interface KanbanBoardCardService {

    KanbanBoardCard create(String content, Member member, KanbanBoard board);


    KanbanBoardCard createCard(Long memberId, String title, String content);
}
