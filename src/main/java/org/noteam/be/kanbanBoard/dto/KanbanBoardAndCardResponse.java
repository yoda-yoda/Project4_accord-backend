package org.noteam.be.kanbanBoard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanbanBoardAndCardResponse {

    List<KanbanBoardResponse> kanbanBoards;

}
