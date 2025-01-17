package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;
import org.noteam.be.kanbanBoard.repository.KanbanBoardCardRepository;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardCardService;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.teamMember.domain.TeamMember;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KanbanBoardCardServiceImpl implements KanbanBoardCardService {

    private final KanbanBoardCardRepository kanbanBoardCardRepository;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;
    private final KanbanBoardService kanbanBoardService;

    @Override
    public KanbanBoardCard create(String content, Member member, KanbanBoard board) {

        KanbanBoardCard boardCard = KanbanBoardCard.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
         return kanbanBoardCardRepository.save(boardCard);

    }



    //Card 추가 로직
    @Override
    public KanbanBoardCard createCard(Long memberId, String title, String content) {
        // id로 멤버 조회
        Member member = memberService.getByMemberId(memberId);
        log.info("member = {}", member);

        // teamMember 조회
        TeamMember teamMemberInfo = teamMemberService.getTeamMember(member);
        log.info("teamMemberInfo = {}", teamMemberInfo);

        //teamid 조회
        Long teamId = teamMemberInfo.getTeam().getId();
        log.info("teamId = {}", teamId);

        //해당 보드 조회
        KanbanBoard byTeamIdAndBoardName = kanbanBoardService.findKanbanBoardbyTeamIdAndTitle(teamId, title);
        log.info("byTeamIdAndBoardName = {}", byTeamIdAndBoardName);


        //보드 카드 추가
        return create(content,member,byTeamIdAndBoardName);
    }




}

