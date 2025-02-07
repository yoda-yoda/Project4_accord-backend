package org.noteam.be.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.comment.domain.Status;
import org.noteam.be.comment.dto.CommentRegisterRequest;
import org.noteam.be.comment.dto.CommentResponse;
import org.noteam.be.comment.dto.CommentUpdateRequest;
import org.noteam.be.comment.repository.CommentRepository;
import org.noteam.be.comment.service.CommentService;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.comment.CommentNotFoundException;
import org.noteam.be.system.exception.joinBoard.JoinBoardNotFoundException;
import org.noteam.be.system.exception.member.MemberNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {


    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final JoinBoardRepository joinBoardRepository;



    // 메서드 기능: 댓글 입력 dto, 멤버, 조인보드id를 매개변수로 받는다. 조인보드id는 내부메서드의 매개변수로 전달하여 해당 조인보드 엔티티를 찾는다.
    // 그리고 toEntity 메서드를 활용해 댓글 엔티티를 만들고 댓글 DB에 저장한다.
    // 예외: 내부 메서드에서 조인보드를 찾지못할시 예외가 발생한다.
    // 반환: 저장한 Comment 엔티티를 CommentResponse 로 변환하고 반환한다.
    @Transactional
    public CommentResponse createCommentByDto(CommentRegisterRequest dto, Member member, Long joinBoardId,  Long parentCommentId) {

        JoinBoard findJoinBoard = getJoinBoardEntityById(joinBoardId);

        Comment comment = CommentRegisterRequest.toEntity(dto, member, findJoinBoard, parentCommentId);

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.fromEntity(savedComment);

    }


    // 메서드 기능: id로 Comment 엔티티를 찾아온다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = delete 여도 반환): Comment 엔티티를 반환한다.
    public Comment getCommentEntityById(Long id) {

        return commentRepository.findById(id)
                .orElseThrow( () -> new CommentNotFoundException( ExceptionMessage.Comment.COMMENT_NOT_FOUND_ERROR) );

    }


    // 메서드 기능: id로 Comment 엔티티를 찾아온다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = ACTIVE 만): 즉 딜리트처리 안된 Comment 엔티티를 반환한다.
    public Comment getCommentEntityByIdWithNoDeleted(Long id) {

        return commentRepository.findById(id)
                .filter( entity -> entity.getStatus().equals(Status.ACTIVE) )
                .orElseThrow( () -> new CommentNotFoundException( ExceptionMessage.Comment.COMMENT_NOT_FOUND_ERROR) );
    }



    // 메서드 기능: id로 Comment 엔티티를 찾고 응답dto로 반환한다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = ACTIVE 만): 딜리트처리 안된 Comment 엔티티를 찾고 응답dto로 바꿔 반환한다.
    public CommentResponse getCommentById(Long id) {

        Comment find = commentRepository.findById(id)
                .filter( entity -> entity.getStatus().equals(org.noteam.be.comment.domain.Status.ACTIVE) )
                .orElseThrow( () -> new CommentNotFoundException( ExceptionMessage.Comment.COMMENT_NOT_FOUND_ERROR) );

        return CommentResponse.fromEntity(find);
    }


    // 메서드 기능: DB에서 글을 전부 찾아 응답으로 변환후 리스트에 담아 반환한다.
    // 예외: 없다. 즉 찾지못해도 예외없이 빈 리스트를 반환한다.
    // 반환: 응답dto들을 List에 담아 반환한다.
    // Status: ACTIVE 만 찾아온다.
    public List<CommentResponse> getAllComment() {

        // 최종 반환 리스트를 미리 만듬
        List<CommentResponse> responses = new ArrayList<>();

        List<Comment> collect = commentRepository.findAll()
                .stream()
                .filter( entity -> entity.getStatus().equals(org.noteam.be.comment.domain.Status.ACTIVE) )
                .collect(Collectors.toList());


        // 해당값이 없으면 빈 리스트를 반환하고 종료
        if (collect.isEmpty()) {
            return responses;
        }

        // 찾은 엔티티들이 있다면 각각 응답으로 변환후 최종 반환할 변수에 add한다.
        for (Comment comment : collect) {
            CommentResponse commentResponse = CommentResponse.fromEntity(comment);
            responses.add(commentResponse);
        }

        return responses;

    }



    // 메서드 기능: DB에서, 해당 JoinBoard에 속하는 댓글 엔티티를 전부 찾아 응답으로 변환후 리스트에 담아 반환한다.
    // 예외: 없다. 즉 찾지못해도 예외없이 빈 리스트를 반환한다.
    // 반환: 응답dto들을 List에 담아 반환한다.
    // Status: ACTIVE 만 찾아온다.
    public List<CommentResponse> getAllCommentByJoinBoardId(Long joinBoardId) {

        // 최종 반환 리스트를 미리 만듬
        List<CommentResponse> responses = new ArrayList<>();

        List<Comment> collect = commentRepository.findAllByJoinBoardId(joinBoardId)
                .stream()
                .filter( entity -> entity.getStatus().equals(org.noteam.be.comment.domain.Status.ACTIVE) )
                .collect(Collectors.toList());

        // 해당값이 없으면 빈 리스트를 반환하고 종료
        if (collect.isEmpty()) {
            return responses;
        }

        // 찾은 엔티티들이 있다면 각각 응답으로 변환후 최종 반환할 변수에 add한다.
        for (Comment comment : collect) {
            CommentResponse commentResponse = CommentResponse.fromEntity(comment);
            responses.add(commentResponse);
        }

        return responses;

    }




    // 메서드 기능: 업데이트 dto를 받아와서 기존 Comment 엔티티를 수정한다.
    // 참고: 기존 엔티티를 조회할때 이곳 내부 메서드를 활용했다.
    // 예외: 내부 메서드에 예외가 내재되어있다.
    // 반환: 수정한 엔티티를 응답 dto로 바꾼뒤 반환한다.
    @Transactional
    public CommentResponse updateCommentById(Long id, CommentUpdateRequest dto) {

        Comment entity = getCommentEntityByIdWithNoDeleted(id);
        entity.updateFromDto(dto);

        // 이렇게 해야, 이 메서드가 종료되기전에 @PreUpdate 메서드가(엔티티 내부에 있는 변경될때 시간 최신화하는 메서드가) 실행되어서 시간이 최신화된다.
        commentRepository.saveAndFlush(entity);

        return CommentResponse.fromEntity(entity);
    }


    // 메서드 기능: 해당 id의 Comment를 소프트 딜리트 처리한다. 만약 ACTIVE인 자식댓글이 존재한다면 그들도 전부 딜리트 처리한다. 대댓글까지만 허용되는 정책이라서 가능한 처리이다.
    // 소프트 딜리트 방식: Enum 타입인 status 변수를, DELETE로 변경한다.
    // 참고: 기존 엔티티를 조회할때 이곳 내부 메서드를 활용했다.
    // 예외: 내부 메서드에 예외가 내재되어있다.
    // 반환: X
    @Transactional
    public void deleteCommentById(Long id) {


        // id로 해당 댓글을 찾아온다.
        Comment entity = getCommentEntityByIdWithNoDeleted(id);


        // 해당 댓글이 부모 뎁스의 댓글이라면 진입한다.
        if (entity.getParentCommentId() == null) {


            // ACTIVE인 자식댓글들을 찾아 list에 담는다.
            List<Comment> list = commentRepository.findAllByParentCommentIdAndStatus(entity.getId(), Status.ACTIVE);



            // 비어있다면(ACTIVE 인 자식댓글이 없다면) 부모댓글 본인만 DELETE 처리하고 메서드를 종료한다
            if ( list.isEmpty() ) {
                entity.changeStatus(org.noteam.be.comment.domain.Status.DELETE);
                return;
            }


            // 자식댓글이 존재한다면 그들도 전부 DELETE 처리하고 저장한다
            for (Comment findComment : list) {
                findComment.changeStatus(org.noteam.be.comment.domain.Status.DELETE);
            }

            // 마지막에 부모댓글 본인만 DELETE 처리하고 종료한다.
            entity.changeStatus(org.noteam.be.comment.domain.Status.DELETE);
            return;


        }

        
        // 여기 도달한다는것은 해당 댓글이 대댓글인거기 때문에, 본인만 DELETE 처리하고 종료한다.
        entity.changeStatus(org.noteam.be.comment.domain.Status.DELETE);

    }





    // 메서드 기능: 멤버 id를 통해 멤버 엔티티를 찾는다. 이 서비스 파일에서 사용하고자 만들었다.
    // 예외: 해당 멤버가 없다면 예외를 던진다.
    // 반환: 해당하는 멤버 Member 엔티티를 반환한다.
    public Member findMemberById(Long currentMemberId) {

        return memberRepository.findById(currentMemberId)
                .orElseThrow( () -> new MemberNotFound(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND) );
    }


    // 메서드 기능: id로 JoinBoard 엔티티를 찾아온다.
    // 예외: 찾지못하면 예외를 던진다.
    // 반환(status = delete 여도 반환): JoinBoard 엔티티를 반환한다.
    public JoinBoard getJoinBoardEntityById(Long id) {

        return joinBoardRepository.findById(id)
                .orElseThrow( () -> new JoinBoardNotFoundException( ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR) );

    }




}
