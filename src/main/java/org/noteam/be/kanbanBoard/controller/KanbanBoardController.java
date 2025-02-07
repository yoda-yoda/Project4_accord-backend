package org.noteam.be.kanbanBoard.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "칸반보드", description = "칸반보드 API")
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
    @Operation(summary = "칸반보드 조회", description = "팀id에 해당하는 칸반보드 조회")
    @GetMapping("/kanbanboard/{teamId}")
    public ResponseEntity<KanbanBoardAndCardResponse> getKanbanBoardList(@PathVariable Long teamId) {

        // 팀 리스트에 teamid가 있다면 아래의 로직을 포함한다.
        KanbanBoardAndCardResponse result = kanbanBoardService.findByTeamId(teamId);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "칸반보드 팀 id 조회", description = "칸반보드 조회를 위한 팀 id 조회")
    @GetMapping("/kanbanboard/team/{teamId}")
    public ResponseEntity<KanbanBoardTeamResponse> getTeam(@PathVariable Long teamId) {

        KanbanBoardTeamResponse result = teamService.getTeamForKanbanBoard(teamId);

        return ResponseEntity.ok(result);
    }


    //칸반 보드 추가
    @Operation(summary = "칸반보드 추가", description = "팀id에 해당하는 칸반보드 추가")
    @PostMapping("/kanbanboard")
    public ResponseEntity<KanbanBoardCreateResponse> createKanbanBoard(@RequestBody KanbanBoardCreateRequest request) {

        KanbanBoardCreateResponse result = kanbanBoardService.createBoard(request);

        return ResponseEntity.ok(result);
    }

    //칸반 보드 카드 추가 로직
    @Operation(summary = "카드 추가", description = "칸반보드 칼럼에 카드 추가")
    @PostMapping("/kanbanboardcard")
    public ResponseEntity<KanbanBoardCardCreateResponse> createKanbanBoardCard(@RequestBody KanbanBoardCardCreateRequest request) {

        log.info("😀request = {}", request);

        KanbanBoardCardCreateResponse result = kanbanBoardCardService.createCard(request);

        return ResponseEntity.ok(result);


    }

    //테스트 코드 작성해야합니다 ..
    // 칸반보드 삭제
    @Operation(summary = "칸반보드 삭제", description = "칸반보드 삭제")
    @DeleteMapping("/kanbanboard")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoard(@RequestBody KanbanBoardDeleteRequest request) {

        Long boardId = request.getBoardId();

        KanbanBoardMessageResponse result = kanbanBoardService.deleteBoard(boardId);

        return ResponseEntity.ok(result);
    }


    //칸반보드 카드 삭제
    @Operation(summary = "카드 삭제", description = "칸반보드 칼럼의 카드 삭제")
    @DeleteMapping("/kanbanboardcard")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoardCard(@RequestBody KanbanBoardCardDeleteRequest request) {

        Long cardId = request.getCardId();

        log.info("😀cardId = {}", cardId);

        KanbanBoardMessageResponse result = kanbanBoardCardService.deleteBoardCard(cardId);

        return ResponseEntity.ok(result);

    }

    //칸반보드 제목 변경
    @Operation(summary = "제목 변경", description = "칸반보드 칼럼이름 변경")
    @PutMapping("/kanbanboard")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardTitle(@RequestBody KanbanBoardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardService.updateBoard(request);

        return ResponseEntity.ok(result);

    }

    //칸반보드 카드 업데이트
    @Operation(summary = "카드 이름 변경", description = "칼럼 내의 카드 이름 변경")
    @PutMapping("/kanbanboardcard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardCard(@RequestBody KanbanBoardCardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.updateCard(request);

        return ResponseEntity.ok(result);

    }

    // 보드 순서 변경
    @Operation(summary = "칼럼 재배치", description = "칼럼 위치 재배치")
    @PostMapping("/kanbanboard/switch")
    public ResponseEntity<KanbanBoardAndCardResponse> changeBoardPriority (@RequestBody KanbanBoardSwitchRequest request) {
        log.info("😂request = {}", request);
        KanbanBoardAndCardResponse result = kanbanBoardService.changeBoardPriority(request);

        return ResponseEntity.ok(result);
    }

    // 카드 순서 변경
    @Operation(summary = "카드 재배치", description = "카드 위치 재배치")
    @PostMapping("/kanbanboardcard/switch")
    public ResponseEntity<KanbanBoardAndCardResponse> changeBoardCardPriority (@RequestBody KanbanBoardCardSwitchRequest request) {
        log.info("😃request = {}", request);
        KanbanBoardAndCardResponse result = kanbanBoardCardService.changeCardPriority(request);
        return ResponseEntity.ok(result);
    }

}






