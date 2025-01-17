package org.noteam.be.kanbanBoard.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
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
    public String createKanbanBoard(@RequestParam Long memberId,@RequestParam String title) {

        kanbanBoardService.createBoard(memberId,title);

        return "success";
    }

    //칸반 보드 카드 추가
    @ResponseBody
    @PostMapping("/kanbanboardcard/create")
    public String createKanbanBoardCard(@RequestParam Long memberId,@RequestParam String title
            ,@RequestParam String content) {

        kanbanBoardCardService.createCard(memberId,title,content);

        return "success";

    }

}
