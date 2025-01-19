package org.noteam.be.joinBoard.service;

import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;

import java.util.List;

public interface JoinBoardService {


    JoinBoardResponse createJoinBoardByDto(JoinBoardRegisterRequest dto);

    JoinBoard getJoinBoardEntityById(Long id);

    JoinBoard getJoinBoardEntityByIdWithNoDeleted(Long id);

    JoinBoardResponse getJoinBoardById(Long id);

    List<JoinBoardResponse> getAllJoinBoard();

    List<JoinBoardCardResponse> getAllJoinBoardCard();

    JoinBoardResponse updateJoinBoardById(Long id, JoinBoardUpdateRequest dto);

    void deleteJoinBoardById(Long id);

}