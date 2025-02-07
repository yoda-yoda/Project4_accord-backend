package org.noteam.be.kanbanBoard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.kanbanBoard.domain.KanbanBoard;
import org.noteam.be.kanbanBoard.dto.*;
import org.noteam.be.kanbanBoard.repository.KanbanBoardRepository;
import org.noteam.be.kanbanBoard.service.KanbanBoardService;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.kanbanboard.KanbanboardNotFoundException;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.service.TeamService;
import org.noteam.be.teamMember.domain.TeamMember;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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
    public KanbanBoardAndCardResponse findByTeamId(Long teamId) {
        List<KanbanBoard> allTeamBoards = getAllTeamBoards(teamId);
        return KanbanBoardAndCardResponse.builder()
                .kanbanBoards(allTeamBoards.stream().map(KanbanBoardResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }



    @Override
    public KanbanBoard getKanbanBoardById(Long currentBoardId) {
        return kanbanBoardRepository.findById(currentBoardId).orElseThrow(() -> new KanbanboardNotFoundException(ExceptionMessage.Kanbanboard.KANBANBOARD_NOT_FOUND_ERROR));
    }

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
    public KanbanBoard getKanbanBoardbyTeamIdAndTitle(KanbanBoardLookupRequest request) {
        return kanbanBoardRepository.findByTeamIdAndTitle(request.getTeamId(), request.getTitle());
    }

    @Override
    public KanbanBoard getKanbanBoardByTeamIdAndBoardId(KanbanBoardSecondLookupRequest request) {
        return kanbanBoardRepository.findByTeamIdAndId(request.getTeamId(), request.getBoardId());
    }

    @Override
    public KanbanBoard getKanbanBoardbyBoardId(Long boardId) {
        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(boardId).orElseThrow();
        return kanbanBoard;
    }


    @Override
    public KanbanBoardCreateResponse createBoard(KanbanBoardCreateRequest request) {

        int num = 0;

        //teamid 조회
        Team team = teamService.getTeamById(request.getTeamId());
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
                .title(request.getTitle())
                .team(team)
                .priority((long) num)
                .build();

        kanbanBoardRepository.save(board);

        return KanbanBoardCreateResponse.builder()
                .columnId(board.getId())
                .title(request.getTitle())
                .build();
    }


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
    public KanbanBoardMessageResponse updateBoard(KanbanBoardUpdateRequest request) {

        KanbanBoard kanbanBoard = kanbanBoardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("KanbanBoard not found with id: " + request.getBoardId()));

        // 더티 체킹 적용: 엔티티의 값만 변경
        kanbanBoard.setTitle(request.getTitle());

        return KanbanBoardMessageResponse.builder()
                .message("Success update Board")
                .result(true)
                .build();

        // 트랜잭션 종료 시 자동으로 업데이트 수행 (더티 체킹)
    }

    //수정 해야된다.
    @Override
    public KanbanBoardAndCardResponse changeBoardPriority(KanbanBoardSwitchRequest request) {

        // 전체 보드 리스트를 조회한다.
        List<KanbanBoard> kanbanBoardList = getKanbanBoardList(request.getTeamId());
        // 타켓 보드를 조회한다.
        KanbanBoard targetBoard = getKanbanBoardbyBoardId(request.getBoardId());

        int currentPriority = targetBoard.getPriority().intValue();

        int newPriority = request.getNewPriority();

        if (currentPriority == newPriority) {
            return findByTeamId(request.getTeamId());
        }

        // 반복을 한다.
        /* 1. board에서 priority를 가져온다.
        *  2. 만약 current priority가 new priority보다 작다면
        * */
        for (KanbanBoard board : kanbanBoardList) {

            int priority = board.getPriority().intValue();
            // 만약 기존 priority 가 1이고 대상 priority가 3이라면
            // 기존 기존 2-> 1 , 기존 3 -> 2 으로 하나씩 내려준다.

            if (currentPriority < newPriority) {

                if (priority > currentPriority && priority <= newPriority) {
                    board.setPriority((long) priority - 1);
                }

            } else {
                if ( priority < currentPriority && priority >= newPriority) {
                    board.setPriority((long) priority + 1);
                }
            }
        }

        targetBoard.setPriority((long) newPriority);

        kanbanBoardRepository.saveAll(kanbanBoardList);
        kanbanBoardRepository.save(targetBoard);
        kanbanBoardRepository.flush();


        return  findByTeamId(request.getTeamId());

    }

}
