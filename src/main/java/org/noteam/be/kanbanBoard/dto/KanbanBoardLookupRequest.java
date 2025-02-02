package org.noteam.be.kanbanBoard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanbanBoardLookupRequest {

    Long teamId;
    String title;

}
