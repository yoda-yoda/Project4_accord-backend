package org.noteam.be.kanbanBoard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class KanbanBoardMessageResponse {

    private String message;
    private boolean result;

    @Builder
    public KanbanBoardMessageResponse(String message, boolean result) {
        this.message = message;
        this.result = result;
    }

}




