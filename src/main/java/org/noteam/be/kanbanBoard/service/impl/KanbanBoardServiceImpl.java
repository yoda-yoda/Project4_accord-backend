package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.team.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KanbanBoardServiceImpl implements KanbanBoardService {

    private final KanbanBoardRepository kanbanBoardRepository;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;


    @Override
    public void createBoard(Team team, String title) {

        KanbanBoard board = KanbanBoard.builder()
                .team(team)
                .title(title)
                .build();
        kanbanBoardRepository.save(board);
    }

    @Override
    public List<KanbanBoard> getAllTeamBoards(Long teamId) {
        return kanbanBoardRepository.findByTeamId(teamId);
    }

    @Override
    public List<KanbanBoard> getKanbanBoardList(Long id) {
        Member memberinfo = memberService.getByMemberId(id);

        TeamMember teamMemberInfo = teamMemberService.getTeamMember(memberinfo);

        Long teamId = teamMemberInfo.getTeam().getId();

        return  getAllTeamBoards(teamId);

    }

    @Override
    public List<KanbanBoard> getBoardList(Long teamId) {
        return kanbanBoardRepository.findByTeamId(teamId);
    }


    // 타이틀, 팀id로 해당 칸반 보드 조회
    @Override
    public KanbanBoard findKanbanBoardbyTeamIdAndTitle(Long teamId,String title) {
        return kanbanBoardRepository.findByTeamIdAndTitle(teamId, title);
    }

    @Override
    public KanbanBoard createBoard(Long memberId, String title) {

        Member member = memberService.getByMemberId(memberId);
        log.info("member = {}", member);

        // teamMember 조회
        TeamMember teamMemberInfo = teamMemberService.getTeamMember(member);
        log.info("teamMemberInfo = {}", teamMemberInfo);

        //teamid 조회
        Team team = teamMemberInfo.getTeam();
        log.info("team = {}", team);

        KanbanBoard board = KanbanBoard.builder()
                .title(title)
                .team(team)
                .build();

        return kanbanBoardRepository.save(board);
    }



}
