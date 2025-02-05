package org.noteam.be.kanbanBoard.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KanbanBoardCardSwitchRequest {

    Long teamId;
    Long cardId;
    Long currentBoardId;
    int newPriority;
    Long newBoardId;

}
