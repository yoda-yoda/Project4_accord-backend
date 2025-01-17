package org.noteam.be.kanbanBoard.service;

import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.member.domain.Member;

public interface KanbanBoardCardService {

    void create(String content, Member member, KanbanBoard board);

}
