package org.noteam.be.dao;

import org.noteam.be.domain.Team;
import org.noteam.be.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
