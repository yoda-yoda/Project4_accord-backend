package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.dto.KanbanBoardMessageResponse;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.service.TeamService;
import org.noteam.be.teamMember.domain.TeamMember;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.data.domain.Sort;
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
    private final TeamService teamService;



    @Override
    public List<KanbanBoard> getAllTeamBoards(Long teamId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        return kanbanBoardRepository.findByTeamId(teamId,sort);
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
        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        return kanbanBoardRepository.findByTeamId(teamId,sort);
    }


    // 타이틀, 팀id로 해당 칸반 보드 조회
    @Override
    public KanbanBoard getKanbanBoardbyTeamIdAndTitle(Long teamId, String title) {
        return kanbanBoardRepository.findByTeamIdAndTitle(teamId, title);
    }

    @Override
    public KanbanBoard getKanbanBoardbyBoardId(Long boardId) {
        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(boardId).orElseThrow();
        return kanbanBoard;
    }


    @Override
    public KanbanBoardMessageResponse createBoard(Long teamid, String title) {

        int num = 0;

        //teamid 조회
        Team team = teamService.getTeamById(teamid);
        log.info("team = {}", team);

        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        List<KanbanBoard> byTeamId = kanbanBoardRepository.findByTeamId(team.getId(),sort);

        if (byTeamId == null || byTeamId.isEmpty()) {
            num = 1;
            log.info("byTeamID is null num = {}", num);
        }

        if (byTeamId != null || byTeamId.size() > 0) {
            num = byTeamId.size() + 1;
            log.info("btTeamID is not null num = {}", num);
        }

        KanbanBoard board = KanbanBoard.builder()
                .title(title)
                .team(team)
                .priority((long) num)
                .build();

        kanbanBoardRepository.save(board);

        return KanbanBoardMessageResponse.builder()
                .message("Success Create Board")
                .result(true)
                .build();
    }

    // 내일 수정 해야함
    @Override
    public KanbanBoardMessageResponse deleteBoard(Long boardId) {

        KanbanBoard kanbanBoard = getKanbanBoardbyBoardId(boardId);
        Team team = kanbanBoard.getTeam();
        Long teamId = team.getId();

        kanbanBoardRepository.deleteById(boardId);

        Sort sort = Sort.by(Sort.Direction.ASC, "priority");
        List<KanbanBoard> all = kanbanBoardRepository.findByTeamId(teamId,sort);

        Long i = 1L;

        for(KanbanBoard board : all) {
            board.setPriority(i++);
        }

        return KanbanBoardMessageResponse.builder()
                .message("Success Delete Board")
                .result(true)
                .build();

    }

    @Override
    public KanbanBoardMessageResponse updateBoard(Long id, String title) {

        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("KanbanBoard not found with id: " + id));

        // 더티 체킹 적용: 엔티티의 값만 변경
        kanbanBoard.setTitle(title);

        return KanbanBoardMessageResponse.builder()
                .message("Success update Board")
                .result(true)
                .build();

        // 트랜잭션 종료 시 자동으로 업데이트 수행 (더티 체킹)
    }


    @Override
    public KanbanBoardMessageResponse changeBoardPriority(Long boardId, int dropSpotNum, Long teamId) {

        KanbanBoard fromKanbanBoard = getKanbanBoardbyBoardId(boardId);
        List<KanbanBoard> kanbanBoardList = getKanbanBoardList(teamId);

        for (int i = dropSpotNum; i <= kanbanBoardList.size(); i++ ) {

            KanbanBoard changeBoard = kanbanBoardList.get(i - 1);
            changeBoard.setPriority((long) (i + 1));

        }

        fromKanbanBoard.setPriority((long) dropSpotNum);

        return  KanbanBoardMessageResponse.builder()
                .message("Sucess Board Position Change")
                .result(true)
                .build();

    }





}
