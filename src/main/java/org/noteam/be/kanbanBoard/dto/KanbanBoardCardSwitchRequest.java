package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanBoardCardSwitchRequest {

    Long cardId;
    Long currentBoardId;
    int newPriority;
    Long newBoardId;

}
