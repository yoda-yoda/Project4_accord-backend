package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KanbanBoardCardCreateRequest {

    Long memberId;
    Long teamId;
    String title;
    String content;

}
