package org.noteam.be.team.repository;

import org.noteam.be.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT DISTINCT t FROM Team t JOIN t.teamMembers tm WHERE tm.member.memberId = :memberId AND t.deleted = false")
    List<Team> findTeamsByMemberId(@Param("memberId") Long memberId);

}
