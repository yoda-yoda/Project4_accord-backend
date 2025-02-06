package org.noteam.be.kanbanBoard.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.noteam.be.kanbanBoard.anotation.CheckTeam;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.member.service.MemberService;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.kanbanboard.KanbanBoardTeamIdNotProvidedException;
import org.noteam.be.system.util.SecurityUtil;
import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamResponse;
import org.noteam.be.team.service.TeamService;
import org.noteam.be.teamMember.domain.TeamMember;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TeamCheckAspect {

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final MemberService memberService;

    @Around("@annotation(checkTeam)")
    public Object teamCheck(ProceedingJoinPoint joinPoint, CheckTeam checkTeam) throws Throwable {


        Long teamId = null;

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();


        for (int i = 0; i < method.getParameters().length; i++) {

            String parameterName = method.getParameters()[i].getName();

            if (parameterName.equals("teamId")) {
                teamId = (Long) joinPoint.getArgs()[i];
                break;
            }

        }


        if (teamId == null) {
            throw new KanbanBoardTeamIdNotProvidedException(ExceptionMessage.Kanbanboard.KANBANBOARD_TEAM_ID_NOT_PROVIDED);
        }

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberService.getByMemberId(currentMemberId);
        Team team = teamService.getTeamById(teamId);

        TeamMember teamMember = teamMemberService.getTeamMemberByMemberAndTeam(member, team);

        if (teamMember != null) {
            return joinPoint.proceed();
        }

        throw new SecurityException("접근 권한이 없습니다. " + " 팀에 소속되지 않았습니다.");

    }

}
