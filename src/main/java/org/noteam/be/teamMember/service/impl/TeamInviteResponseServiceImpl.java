package org.noteam.be.teamMember.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.system.exception.EmailSendException;
import org.noteam.be.system.exception.FindNotTeamException;
import org.noteam.be.teamMember.service.TeamInviteResponseService;
import org.noteam.be.teamMember.repository.MemberRepository;
import org.noteam.be.teamMember.repository.TeamMemberRepository;

import org.noteam.be.teamMember.domain.Member;
import org.noteam.be.teamMember.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;

import org.noteam.be.teamMember.dto.InviteMemberResponce;

import org.noteam.be.system.exception.ExceptionMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TeamInviteResponseServiceImpl implements TeamInviteResponseService {

    private final TeamServiceImpl teamServiceImpl;
    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberServiceImpl teamMemberServiceImpl;
    private final InviteMemberResponce inviteMemberResponce = new InviteMemberResponce();


    @Override
    public InviteMemberResponce AcceptTeamInvite(Long teamId, Long memberId) {

        Team team = teamServiceImpl.findByteamId(teamId)
                .orElseThrow(() -> new FindNotTeamException(ExceptionMessage.EMPTY_TEAM));

        List<TeamMember> teamMembers = team.getTeamMembers();

        Member member = memberRepository.findById(memberId).get();

        // 기존 멤버아이디가 조회 안되는지 확인하고 조회가 알될경우 save실행
        List<TeamMember> findMember = teamMemberRepository.findByMember(member);

        if (findMember.isEmpty()) {

            if (teamMembers.size() <= 3 ) {
                teamMemberServiceImpl.save(member,team);
                return inviteMemberResponce.builder()
                        .message("Success Add Team Member")
                        .result(true)
                        .build();
            }

            return inviteMemberResponce.builder()
                    .message("Faild Add Team Member")
                    .result(false)
                    .build();

        }
        return inviteMemberResponce.builder()
                    .message("Already Invited Team Member")
                    .result(false)
                    .build();

    }

}

