package org.noteam.be.teamMember.service;

import org.noteam.be.member.domain.Member;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.team.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;

import java.util.List;

public interface TeamMemberService {

    public List<MemberProfileResponse> findMembersByTeamId(Long teamId);

    public List<MemberProfileResponse> findMembersByEmail(String query, Long teamId);

    TeamMember save (Member member, Team team);

    TeamMember getTeamMember(Member member);

    TeamMember getTeamMemberByMemberAndTeam(Member member, Team team);
}
