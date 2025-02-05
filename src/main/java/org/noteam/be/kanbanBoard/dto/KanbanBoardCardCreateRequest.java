package org.noteam.be.kanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KanbanBoardCardCreateRequest {

    Long memberId;
    Long teamId;
    Long columnId;
    String content;

}
