package org.noteam.be.kanbanBoard.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KanbanBoardDeleteRequest {
    private Long boardId;
}