package org.noteam.be.joinBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.joinBoard.service.Impl.JoinBoardServiceImpl;
import org.noteam.be.joinBoard.service.JoinBoardService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/joinBoard")
@RequiredArgsConstructor
public class JoinBoardController {


    private final JoinBoardService joinBoardService;


    // 게시판 목록 페이지를 page 처리해서 조회
    // 경로예시: /joinBoard/list?page=0
    @GetMapping("/list")
    public String getJoinBoardList(Model model, @RequestParam(value = "page", defaultValue="0") int page
                                   ) {
        // 전체 게시글 목록을 카드 형태로 가져옵니다.
        Page<JoinBoardCardResponse> paging = joinBoardService.getAllJoinBoardCardByPage(page);
        model.addAttribute("joinBoardList", paging);

        return "joinBoard/list";
    }




//    // 게시판 목록 페이지
//    @GetMapping("/list")
//    public String getJoinBoardList(Model model) {
//        // 전체 게시글 목록을 카드 형태로 가져옵니다.
//        var joinBoardList = joinBoardService.getAllJoinBoardCard();
//        model.addAttribute("joinBoardList", joinBoardList);
//
//        return "joinBoard/list";
//    }






    // 게시판 상세 페이지 (글 클릭 시)
    @GetMapping("/{id}")
    public String getJoinBoardDetail(@PathVariable Long id, Model model) {
        JoinBoardResponse joinBoardResponse = joinBoardService.getJoinBoardById(id);
        model.addAttribute("joinBoard", joinBoardResponse);
        return "joinBoard/detail";
    }

    // 게시판 글 등록 페이지
    @GetMapping("/new")
    public String createJoinBoardForm(Model model) {
        model.addAttribute("joinBoardRegisterRequest", new JoinBoardRegisterRequest());
        return "joinBoard/create";
    }

    // 게시판 글 등록 처리
    @PostMapping("/new")
    public String createJoinBoard(@Validated @ModelAttribute JoinBoardRegisterRequest joinBoardRegisterRequest,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "joinBoard/create";
        }

        JoinBoardResponse response = joinBoardService.createJoinBoardByDto(joinBoardRegisterRequest);
        return "redirect:/joinBoard/" + response.getId();
    }

    // 게시판 글 수정 페이지
    @GetMapping("/{id}/edit")
    public String updateJoinBoardForm(@PathVariable Long id, Model model) {
        JoinBoardResponse joinBoardResponse = joinBoardService.getJoinBoardById(id);
        JoinBoardUpdateRequest updateRequest = new JoinBoardUpdateRequest(
                joinBoardResponse.getTitle(),
                joinBoardResponse.getTopic(),
                joinBoardResponse.getTeamName(),
                joinBoardResponse.getProjectBio(),
                joinBoardResponse.getTeamBio(),
                joinBoardResponse.getContent(),
                joinBoardResponse.getStartDate(),
                joinBoardResponse.getEndDate(),
                joinBoardResponse.getPeopleNumber()
        );
        model.addAttribute("joinBoardUpdateRequest", updateRequest);
        model.addAttribute("id", id);
        return "joinBoard/edit";
    }



    // 게시판 글 수정 처리
    @PostMapping("/{id}/edit")
    public String updateJoinBoard(@PathVariable Long id,
                                  @Validated @ModelAttribute JoinBoardUpdateRequest joinBoardUpdateRequest,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "joinBoard/edit";
        }

        joinBoardService.updateJoinBoardById(id, joinBoardUpdateRequest);


        return "redirect:/joinBoard/" + id;
    }

    // 게시판 글 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteJoinBoard(@PathVariable Long id) {
        joinBoardService.deleteJoinBoardById(id);
        return "redirect:/joinBoard/list";
    }






}
