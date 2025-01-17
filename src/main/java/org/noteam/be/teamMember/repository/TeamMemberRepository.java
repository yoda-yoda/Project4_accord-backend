package org.noteam.be.teamMember.repository;

import org.noteam.be.member.domain.Member;
import org.noteam.be.teamMember.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    TeamMember findByMember (Member member);
}
