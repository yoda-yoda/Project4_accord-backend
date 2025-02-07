package org.noteam.be.comment.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.comment.dto.CommentRegisterRequest;
import org.noteam.be.comment.dto.CommentResponse;
import org.noteam.be.comment.dto.CommentUpdateRequest;
import org.noteam.be.comment.service.CommentService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.ExistingAuthenticationIsNull;
import org.noteam.be.system.response.ResponseCode;
import org.noteam.be.system.response.ResponseData;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;


    @PostMapping("/members/comment/{joinBoardId}")
    public ResponseEntity<ResponseData<CommentResponse>> createComment(
            @RequestBody CommentRegisterRequest dto,
            @PathVariable Long joinBoardId,
            @RequestParam(required = false) Long parentCommentId
    ) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        if ( currentMemberId==null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member findMember = commentService.findMemberById(currentMemberId);

        CommentResponse res = commentService.createCommentByDto(dto, findMember, joinBoardId, parentCommentId);

        return ResponseData.toResponseEntity(ResponseCode.POST_COMMENT_SUCCESS, res);
    }




    // 해당 조인보드의 ACTIVE인 모든 댓글을 반환한다. //
    // 표시될 댓글이 없다면 빈 리스트가 반환된다.
    @GetMapping("/comment/{joinBoardId}")
    public ResponseEntity<ResponseData<List<CommentResponse>>> getAllComment(@PathVariable Long joinBoardId){

        List<CommentResponse> list = commentService.getAllCommentByJoinBoardId(joinBoardId);

        return ResponseData.toResponseEntity(ResponseCode.GET_COMMENT_SUCCESS, list);
    }


    @GetMapping("/comment/{joinBoardId}/{commentId}")
    public ResponseEntity<ResponseData<CommentResponse>> getComment(@PathVariable Long commentId){

        CommentResponse res = commentService.getCommentById(commentId);

        return ResponseData.toResponseEntity(ResponseCode.GET_COMMENT_SUCCESS, res);
    }



    @PutMapping("/members/comment/{commentId}")
    public ResponseEntity<ResponseData<CommentResponse>> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest dto) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if ( currentMemberId == null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member currentMember = commentService.findMemberById(currentMemberId);

        Comment entity = commentService.getCommentEntityByIdWithNoDeleted(commentId);

        if ( entity.getMember().getMemberId().equals(currentMember.getMemberId()) || currentMember.getRole().equals(Role.ADMIN) ){

            CommentResponse res = commentService.updateCommentById(commentId, dto);

            return ResponseData.toResponseEntity(ResponseCode.UPDATE_COMMENT_SUCCESS, res); //

        } else {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

    }



    @DeleteMapping("/members/comment/{commentId}")
    public ResponseEntity<ResponseData> deleteComment(@PathVariable Long commentId) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if ( currentMemberId == null ) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

        Member currentMember = commentService.findMemberById(currentMemberId);

        Comment entity = commentService.getCommentEntityByIdWithNoDeleted(commentId);

        if ( entity.getMember().getMemberId().equals(currentMember.getMemberId()) || currentMember.getRole().equals(Role.ADMIN) ){

            commentService.deleteCommentById(commentId);

            return ResponseData.toResponseEntity(ResponseCode.DELETE_COMMENT_SUCCESS);

        } else {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.MemberAuth.EXISTING_AUTHENTICATION_IS_NULL);
        }

    }




}
