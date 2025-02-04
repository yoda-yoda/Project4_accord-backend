package org.noteam.be.comment.service;

import org.noteam.be.comment.domain.Comment;
import org.noteam.be.comment.dto.CommentRegisterRequest;
import org.noteam.be.comment.dto.CommentResponse;
import org.noteam.be.comment.dto.CommentUpdateRequest;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.member.domain.Member;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CommentService {

    CommentResponse createCommentByDto(CommentRegisterRequest dto, Member member, Long joinBoardId);

    Comment getCommentEntityById(Long id);

    Comment getCommentEntityByIdWithNoDeleted(Long id);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllComment();

    CommentResponse updateCommentById(Long id, CommentUpdateRequest dto);

    void deleteCommentById(Long id);

    Member findMemberById(Long currentMemberId);

    JoinBoard getJoinBoardEntityById(Long id);

}