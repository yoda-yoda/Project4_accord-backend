package org.noteam.be.kanbanBoard.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KanbanBoardController {

    private final KanbanBoardService kanbanBoardService;
    private final KanbanBoardCardService kanbanBoardCardService;

    // 나중에 로그인 정보로 member id 가져오는 걸로 교체
    // 칸반보드 조회 하는 로직
    @GetMapping("/kanbanboard/{teamId}")
    public ResponseEntity<KanbanBoardAndCardResponse> getKanbanBoardList(@PathVariable Long teamId) {

        KanbanBoardAndCardResponse result = kanbanBoardService.findByTeamId(teamId);

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

        KanbanBoardMessageResponse result = kanbanBoardCardService.createCard(request);

        return ResponseEntity.ok(result);

    }

    //테스트 코드 작성해야합니다 ..
    // 칸반보드 삭제
    @PostMapping("/kanbanboard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoard(@RequestBody KanbanBoardDeleteRequest request) {

        Long boardId = request.getBoardId();

        KanbanBoardMessageResponse result = kanbanBoardService.deleteBoard(boardId);

        return ResponseEntity.ok(result);
    }


    //칸반보드 카드 삭제
    @PostMapping("/kanbanboardcard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoardCard(@RequestBody Long cardId) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.deleteBoardCard(cardId);

        return ResponseEntity.ok(result);

    }

    //칸반보드 제목 변경
    @PostMapping("/kanbanboard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardTitle(@RequestBody KanbanBoardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardService.updateBoard(request);

        return ResponseEntity.ok(result);

    }

    //칸반보드 카드 업데이트
    @PostMapping("/kanbanboardcard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardCard(@RequestBody KanbanBoardCardUpdateRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.updateCard(request);

        return ResponseEntity.ok(result);

    }

    // 보드 순서 변경
    @PostMapping("/kanbanboard/switch")
    public ResponseEntity<KanbanBoardMessageResponse> changeBoardPriority (@RequestBody KanbanBoardSwitchRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardService.changeBoardPriority(request);

        return ResponseEntity.ok(result);
    }

    // 카드 순서 변경
    @PostMapping("/kanbanboardcard/switch")
    public ResponseEntity<KanbanBoardMessageResponse> changeBoardCardPriority (@RequestBody KanbanBoardCardSwitchRequest request) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.changeCardPriority(request);

        return ResponseEntity.ok(result);
    }

}






