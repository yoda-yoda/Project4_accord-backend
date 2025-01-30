package org.noteam.be.kanbanBoard.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class KanbanBoardController {

    private final KanbanBoardService kanbanBoardService;
    private final KanbanBoardCardService kanbanBoardCardService;


    // 나중에 로그인 정보로 member id 가져오는 걸로 교체
    // 칸반보드 조회 하는 로직
    @GetMapping("/kanbanboard/{memberId}")
    public String getKanbanBoardList(@PathVariable Long memberId, Model model) {

        List<KanbanBoard> boards = kanbanBoardService.getKanbanBoardList(memberId);

        model.addAttribute("boards",boards);

        return "kanbanboard";

    }

    //칸반 보드 추가
    @PostMapping("/kanbanboard/create")
    public ResponseEntity<KanbanBoardMessageResponse> createKanbanBoard(@RequestParam Long teamId, @RequestParam String title) {

        KanbanBoardMessageResponse result = kanbanBoardService.createBoard(teamId, title);

        return ResponseEntity.ok(result);
    }

    //칸반 보드 카드 추가
    @ResponseBody
    @PostMapping("/kanbanboardcard/create")
    public ResponseEntity<KanbanBoardMessageResponse> createKanbanBoardCard(@RequestParam Long memberId,@RequestParam Long teamId,@RequestParam String title
            ,@RequestParam String content) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.createCard(memberId,teamId,title,content);

        return ResponseEntity.ok(result);

    }

    // 칸반보드 삭제
    @ResponseBody
    @PostMapping("/kanbanboard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoard(@RequestParam Long boardId) {

        KanbanBoardMessageResponse result = kanbanBoardService.deleteBoard(boardId);

        return ResponseEntity.ok(result);
    }


    //칸반보드 카드 삭제
    @ResponseBody
    @PostMapping("/kanbanboardcard/delete")
    public ResponseEntity<KanbanBoardMessageResponse> deleteKanbanBoardCard(@RequestParam Long cardId) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.deleteBoardCard(cardId);

        return ResponseEntity.ok(result);
    }

    //칸반보드 제목 변경
    @ResponseBody
    @PostMapping("/kanbanboard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardTitle(@RequestParam Long boardId,@RequestParam String title) {

        KanbanBoardMessageResponse result = kanbanBoardService.updateBoard(boardId, title);

        return ResponseEntity.ok(result);

    }

    //칸반보드 카드 업데이트
    @ResponseBody
    @PostMapping("/kanbanboardcard/update")
    public ResponseEntity<KanbanBoardMessageResponse> updateKanbanBoardCard(@RequestParam Long cardId,@RequestParam String content) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.updateCard(cardId, content);

        return ResponseEntity.ok(result);

    }

    // 보드 순서 변경 문제!
    @ResponseBody
    @PostMapping("/kanbanboard/switch")
    public ResponseEntity<KanbanBoardMessageResponse> changeBoardPriority (@RequestParam Long boardId, @RequestParam  int dropSpotNum, @RequestParam Long teamId) {

        KanbanBoardMessageResponse result = kanbanBoardService.changeBoardPriority(boardId, dropSpotNum, teamId);

        return ResponseEntity.ok(result);
    }



    // 카드 순서 변경 문제!
    @ResponseBody
    @PostMapping("/kanbanboardcard/switch")
    public ResponseEntity<KanbanBoardMessageResponse> changeBoardCardPriority (@RequestParam Long cardId,@RequestParam Long boardId ,@RequestParam int dropSpotNum, @RequestParam Long newBoardId) {

        KanbanBoardMessageResponse result = kanbanBoardCardService.changeCardPriority(cardId, boardId, dropSpotNum, newBoardId);
        List<KanbanBoardCard> card = kanbanBoardCardService.getKanbanBoardCardbyBoardId(boardId);
        kanbanBoardCardService.forEachCard(card);

        return ResponseEntity.ok(result);
    }





}






