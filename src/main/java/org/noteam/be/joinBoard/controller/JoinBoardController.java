package org.noteam.be.joinBoard.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.joinBoard.dto.JoinBoardCardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardRegisterRequest;
import org.noteam.be.joinBoard.dto.JoinBoardResponse;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.joinBoard.service.JoinBoardService;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join-board")
@RequiredArgsConstructor
public class JoinBoardController {

    private final JoinBoardService joinBoardService;


    @PostMapping
    public ResponseEntity<ResponseData<JoinBoardResponse>> createJoinBoard(@RequestBody JoinBoardRegisterRequest dto){

        JoinBoardResponse res = joinBoardService.createJoinBoardByDto(dto);

        return ResponseData.toResponseEntity(ResponseCode.POST_JOIN_BOARD_SUCCESS ,res);
    }


    @GetMapping
    public ResponseEntity<ResponseData<Page<JoinBoardCardResponse>>> getPagedList(
            @RequestParam(value = "page", defaultValue = "0") int page)
    {
        Page<JoinBoardCardResponse> pagedList = joinBoardService.getAllJoinBoardCardByPage(page);

        return ResponseData.toResponseEntity(ResponseCode.GET_JOIN_BOARD_SUCCESS, pagedList);
    }


    @GetMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData<JoinBoardResponse>> getJoinBoard(@PathVariable Long joinBoardId){

        JoinBoardResponse res = joinBoardService.getJoinBoardById(joinBoardId);

        return ResponseData.toResponseEntity(ResponseCode.GET_JOIN_BOARD_SUCCESS, res);
    }


    @PutMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData<JoinBoardResponse>> updateJoinBoard(@PathVariable Long joinBoardId, @RequestBody JoinBoardUpdateRequest dto) {

        JoinBoardResponse res = joinBoardService.updateJoinBoardById(joinBoardId, dto);

        return ResponseData.toResponseEntity(ResponseCode.UPDATE_JOIN_BOARD_SUCCESS, res);
    }

    @DeleteMapping("/{joinBoardId}")
    public ResponseEntity<ResponseData> deleteJoinBoard(@PathVariable Long joinBoardId) {

        joinBoardService.deleteJoinBoardById(joinBoardId);

        return ResponseData.toResponseEntity(ResponseCode.DELETE_JOIN_BOARD_SUCCESS);
    }




}
