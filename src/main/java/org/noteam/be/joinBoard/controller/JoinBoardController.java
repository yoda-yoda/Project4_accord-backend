package org.noteam.be.joinBoard.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.service.Impl.JoinBoardServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/join-board")
@RequiredArgsConstructor
public class JoinBoardController {

    // 나중에 impl 바꾸기
    private final JoinBoardServiceImpl joinBoardService;
}
