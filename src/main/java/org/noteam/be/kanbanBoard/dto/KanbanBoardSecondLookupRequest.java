package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanbanBoardSecondLookupRequest {
    Long teamId;
    Long boardId;
}
