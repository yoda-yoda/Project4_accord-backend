package org.noteam.be.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.noteam.be.admin.dto.BoardResponse;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.joinBoard.repository.JoinBoardRepository;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.system.exception.joinBoard.JoinBoardNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.noteam.be.system.exception.ExceptionMessage.JoinBoard.JOIN_BOARD_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final JoinBoardRepository joinBoardRepository;
    private final MemberRepository memberRepository;

    // 회원이 작성한 게시글 조회
    public Page<BoardResponse> getMemberBoards(Long memberId, Pageable pageable) {
        Page<JoinBoard> boards = joinBoardRepository.findByMember_MemberId(memberId, pageable);

        return boards.map(board -> new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getTeamName(),
                board.getStartDate(),
                board.getEndDate()
        ));

    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        joinBoardRepository.deleteById(boardId);
    }

    // 게시글 삭제 + 해당 작성자 밴
    @Transactional
    public void deleteBoardAndBanUser(Long boardId) {
        JoinBoard board= joinBoardRepository.findById(boardId)
                .orElseThrow(() -> new JoinBoardNotFoundException(JOIN_BOARD_NOT_FOUND_ERROR));

        Member member = board.getMember();
        member.changeStatus(Status.BANNED);
        memberRepository.save(member);

        joinBoardRepository.delete(board);

    }

}