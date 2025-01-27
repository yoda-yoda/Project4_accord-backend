package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.member.domain.Member;

@Data
@AllArgsConstructor
public class KanbanBoardCardDto {

    private String content;

    private Member member;

    private KanbanBoard kanbanBoard;


}
