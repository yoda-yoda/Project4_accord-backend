package org.noteam.be.dao;

import org.noteam.be.domain.Member;
import org.noteam.be.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByMember (Member member);
}
