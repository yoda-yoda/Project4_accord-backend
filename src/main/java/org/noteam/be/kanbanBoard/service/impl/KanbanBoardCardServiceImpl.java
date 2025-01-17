package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.repository.KanbanBoardCardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KanbanBoardCardServiceImpl implements KanbanBoardCardService {

    private final KanbanBoardCardRepository kanbanBoardCardRepository;

    @Override
    public void create(String content, Member member, KanbanBoard board) {

        KanbanBoardCard boardCard = KanbanBoardCard.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
         kanbanBoardCardRepository.save(boardCard);

    }
}
