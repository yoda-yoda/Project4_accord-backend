package org.noteam.be.Email.Service;

import org.noteam.be.domain.Member;
import org.noteam.be.domain.Team;
import org.noteam.be.domain.TeamMember;

public interface TeamMemberService {
    TeamMember save (Member member, Team team);
}
