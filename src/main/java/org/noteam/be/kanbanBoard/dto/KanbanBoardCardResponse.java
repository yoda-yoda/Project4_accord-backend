package org.noteam.be.kanbanBoard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanbanBoardCardResponse {

    private Long id;

    private String title;

    private String memberNickName;

    private Long boardId;

    private Long priority;

    public static KanbanBoardCardResponse fromEntity(KanbanBoardCard card) {
        return KanbanBoardCardResponse.builder()
                .id(card.getId())
                .title(card.getContent())
                .memberNickName(card.getMember().getNickname())
                .boardId(card.getBoard().getId())
                .priority(card.getPriority())
                .build();
    }

}


