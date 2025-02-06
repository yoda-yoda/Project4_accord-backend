
package org.noteam.be.joinBoard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.joinBoard.service.JoinBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.ExistingAuthenticationIsNull;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "조인보드", description = "조인보드 게시판 API")
@RestController
@RequestMapping("/api/join-board")
@RequiredArgsConstructor
public class JoinBoardController {


    private final JoinBoardService joinBoardService;


    @Operation(summary = "게시글 작성", description = "JoinBoard 게시판에서 게시글 작성")
    @PostMapping
    public ResponseEntity<ResponseData<JoinBoardResponse>> createJoinBoard(@RequestBody JoinBoardRegisterRequest dto){

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        if ( currentMemberId==null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member findMember = joinBoardService.findMemberById(currentMemberId);

        JoinBoardResponse res = joinBoardService.createJoinBoardByDto(dto, findMember);

        return ResponseData.toResponseEntity(ResponseCode.POST_JOIN_BOARD_SUCCESS ,res);
    }


    @GetMapping
    public ResponseEntity<ResponseData<Page<JoinBoardCardResponse>>> getPagedList(
            @RequestParam(value = "page", defaultValue = "0") int page)
    {
        Page<JoinBoardCardResponse> pagedList = joinBoardService.getAllJoinBoardCardByPage(page);

        return ResponseData.toResponseEntity(ResponseCode.GET_JOIN_BOARD_SUCCESS, pagedList);
    }

    @Operation(summary = "게시글 검색", description = "JoinBoard 게시판에서 제목으로 게시글 검색")
    @GetMapping("/sort-by-title")
    public ResponseEntity<ResponseData<Page<JoinBoardCardResponse>>> getPagedListSortByTitle(
            @RequestParam(value = "page", defaultValue = "0") int page)
    {
        Page<JoinBoardCardResponse> pagedList = joinBoardService.getAllJoinBoardCardByTitle(page);

        return ResponseData.toResponseEntity(ResponseCode.GET_JOIN_BOARD_SUCCESS, pagedList);
    }



    @Operation(summary = "게시글 상세 페이지", description = "게시판 상세 페이지 정보 조회")
    @GetMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData<JoinBoardResponse>> getJoinBoard(@PathVariable Long joinBoardId){

        JoinBoardResponse res = joinBoardService.getJoinBoardById(joinBoardId);

        return ResponseData.toResponseEntity(ResponseCode.GET_JOIN_BOARD_SUCCESS, res);
    }



    @Operation(summary = "게시글 수정", description = "JoinBoard 게시판 작성된 게시글 수정")
    @PutMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData<JoinBoardResponse>> updateJoinBoard(@PathVariable Long joinBoardId, @RequestBody JoinBoardUpdateRequest dto) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if ( currentMemberId == null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member currentMember = joinBoardService.findMemberById(currentMemberId);

        JoinBoard currentJoinBoard = joinBoardService.getJoinBoardEntityById(joinBoardId);


        if ( currentJoinBoard.getMember().equals(currentMember) || currentMember.getRole().equals(Role.ADMIN) ){

            JoinBoardResponse res = joinBoardService.updateJoinBoardById(joinBoardId, dto);

            return ResponseData.toResponseEntity(ResponseCode.UPDATE_JOIN_BOARD_SUCCESS, res);

        } else {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

    }

    @Operation(summary = "게시글 삭제", description = "JoinBoard 게시판 작성된 게시글 삭제")
    @DeleteMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData> deleteJoinBoard(@PathVariable Long joinBoardId) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if ( currentMemberId == null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member currentMember = joinBoardService.findMemberById(currentMemberId);

        JoinBoard currentJoinBoard = joinBoardService.getJoinBoardEntityById(joinBoardId);


        if ( currentJoinBoard.getMember().equals(currentMember) || currentMember.getRole().equals(Role.ADMIN) ){

            joinBoardService.deleteJoinBoardById(joinBoardId);

            return ResponseData.toResponseEntity(ResponseCode.DELETE_JOIN_BOARD_SUCCESS);

        } else {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

    }




}
