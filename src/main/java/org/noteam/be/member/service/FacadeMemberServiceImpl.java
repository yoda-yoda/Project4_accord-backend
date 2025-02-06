package org.noteam.be.member.service;

import lombok.RequiredArgsConstructor;
import org.noteam.be.member.dto.FacadeMemberProfileResponse;
import org.noteam.be.team.service.TeamService;
import org.noteam.be.teamMember.service.TeamMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeMemberServiceImpl implements FacadeMemberService {

    private final MemberService memberService;
    private final TeamService teamService;

    public FacadeMemberProfileResponse getFacadeMemberProfileResponse(Long memberId) {
        return FacadeMemberProfileResponse.fromDto(
                memberService.getMemberProfile(memberId),
                teamService.getTeamsByMemberId(memberId)
        );
    }

}
