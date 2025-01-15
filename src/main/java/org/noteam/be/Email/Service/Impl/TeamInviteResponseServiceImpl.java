package org.noteam.be.Email.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.Email.Service.TeamInviteResponseService;
import org.noteam.be.dao.MemberRepository;
import org.noteam.be.dao.TeamMemberRepository;

import org.noteam.be.domain.Member;
import org.noteam.be.domain.Team;
import org.noteam.be.domain.TeamMember;

import org.noteam.be.dto.InviteMemberResponce;

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

        Team byteamId = teamServiceImpl.findByteamId(teamId)
                .orElseThrow(() -> new RuntimeException(ExceptionMessage.EMPTY_TEAM));

        Team team = byteamId;

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
