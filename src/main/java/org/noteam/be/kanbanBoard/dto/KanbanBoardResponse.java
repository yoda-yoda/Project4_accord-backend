package org.noteam.be.kanbanBoard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanbanBoardResponse {

    private Long id;

    private String name;

    private Long priority;

    List<KanbanBoardCardResponse> cards;

    public static KanbanBoardResponse fromEntity(KanbanBoard kanbanBoard) {
        return KanbanBoardResponse.builder()
                .id(kanbanBoard.getId())
                .name(kanbanBoard.getTitle())
                .priority(kanbanBoard.getPriority())
                .cards(kanbanBoard.getCards()
                    .stream().map(c -> KanbanBoardCardResponse.fromEntity(c))
                    .collect(Collectors.toList()))
                .build();
    }
}
