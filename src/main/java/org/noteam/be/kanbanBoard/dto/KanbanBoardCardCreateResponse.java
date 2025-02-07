package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanbanBoardCardCreateResponse {
    Long cardId;
    String title;
    String memberNickname;
}
