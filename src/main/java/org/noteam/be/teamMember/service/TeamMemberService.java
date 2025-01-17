package org.noteam.be.teamMember.service;

import org.noteam.be.member.domain.Member;
import org.noteam.be.team.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;

public interface TeamMemberService {
    TeamMember save (Member member, Team team);
}
