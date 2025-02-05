package org.noteam.be.kanbanBoard.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.system.exception.team.TeamNotFoundException;
import org.noteam.be.system.util.SecurityUtil;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamResponse;
import org.noteam.be.team.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KanbanBoardController {

    private final KanbanBoardService kanbanBoardService;
    private final KanbanBoardCardService kanbanBoardCardService;
    private final TeamService teamService;

    // aop 추가
    // 나중에 로그인 정보로 member id 가져오는 걸로 교체
    // 칸반보드 조회 하는 로직
    @GetMapping("/kanbanboard/{teamId}")
    public ResponseEntity<KanbanBoardAndCardResponse> getKanbanBoardList(@PathVariable Long teamId) {

        // 팀 리스트에 teamid가 있다면 아래의 로직을 포함한다.
        KanbanBoardAndCardResponse result = kanbanBoardService.findByTeamId(teamId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/kanbanboard/team/{teamId}")
    public ResponseEntity<KanbanBoardTeamResponse> getTeam(@PathVariable Long teamId) {

        KanbanBoardTeamResponse result = teamService.getTeamForKanbanBoard(teamId);

        return ResponseEntity.ok(result);
    }


    //칸반 보드 추가
    @PostMapping("/kanbanboard/create")
    public ResponseEntity<KanbanBoardMessageResponse> createKanbanBoard(@RequestBody KanbanBoardCreateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardService.createBoard(request);

        return ResponseEntity.ok(result);
    }

    //칸반 보드 카드 추가 로직
    @PostMapping("/kanbanboardcard/create")
    public ResponseEntity<KanbanBoardMessageResponse> createKanbanBoardCard(@RequestBody KanbanBoardCardCreateRequest request) {

        log.info("😀request = {}", request);

        KanbanBoardMessageResponse result = kanbanBoardCardService.createCard(request);

        return ResponseEntity.ok(result);


    }

    //테스트 코드 작성해야합니다 ..
    // 칸반보드 삭제
    @DeleteMapping("/kanbanboard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoard(@RequestBody KanbanBoardDeleteRequest request) {

        Long boardId = request.getBoardId();

        KanbanBoardMessageResponse result = kanbanBoardService.deleteBoard(boardId);

        return ResponseEntity.ok(result);
    }


    //칸반보드 카드 삭제
    @DeleteMapping("/kanbanboardcard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoardCard(@RequestBody KanbanBoardCardDeleteRequest request) {

        Long cardId = request.getCardId();

        log.info("😀cardId = {}", cardId);

        KanbanBoardMessageResponse result = kanbanBoardCardService.deleteBoardCard(cardId);

        return ResponseEntity.ok(result);

    }

    //칸반보드 제목 변경
    @PutMapping("/kanbanboard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardTitle(@RequestBody KanbanBoardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardService.updateBoard(request);

        return ResponseEntity.ok(result);

    }

    //칸반보드 카드 업데이트
    @PutMapping("/kanbanboardcard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardCard(@RequestBody KanbanBoardCardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.updateCard(request);

        return ResponseEntity.ok(result);

    }

    // 보드 순서 변경
    @PostMapping("/kanbanboard/switch")
    public ResponseEntity<KanbanBoardAndCardResponse> changeBoardPriority (@RequestBody KanbanBoardSwitchRequest request) {
        log.info("😂request = {}", request);
        KanbanBoardAndCardResponse result = kanbanBoardService.changeBoardPriority(request);

        return ResponseEntity.ok(result);
    }

    // 카드 순서 변경
    @PostMapping("/kanbanboardcard/switch")
    public ResponseEntity<KanbanBoardAndCardResponse> changeBoardCardPriority (@RequestBody KanbanBoardCardSwitchRequest request) {
        log.info("😃request = {}", request);
        KanbanBoardAndCardResponse result = kanbanBoardCardService.changeCardPriority(request);
        return ResponseEntity.ok(result);
    }

}






