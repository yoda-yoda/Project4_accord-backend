package org.noteam.be.teamMember.repository;

import org.noteam.be.member.domain.Member;
import org.noteam.be.team.domain.Team;
import org.noteam.be.teamMember.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    TeamMember findByMember (Member member);

    List<TeamMember> findByTeamIdAndDeletedFalse(Long teamId);

    @Query("SELECT tm.member.memberId FROM TeamMember tm WHERE tm.team.id = :teamId AND tm.deleted = false")
    List<Long> findMemberIdsByTeamIdAndDeletedFalse(@Param("teamId") Long teamId);

    TeamMember findByMemberAndTeam (Member member, Team team);
}
