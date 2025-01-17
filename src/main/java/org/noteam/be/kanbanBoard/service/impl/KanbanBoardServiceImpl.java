package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.team.domain.Team;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class KanbanBoardServiceImpl implements KanbanBoardService {

    private final KanbanBoardRepository kanbanBoardRepository;

    @Override
    public void createBoard(Team team, String title) {

        KanbanBoard board = KanbanBoard.builder()
                .team(team)
                .title(title)
                .build();
        kanbanBoardRepository.save(board);

    }


}
