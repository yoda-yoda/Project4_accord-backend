package org.noteam.be.dao;

import org.noteam.be.domain.Member;
import org.noteam.be.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
