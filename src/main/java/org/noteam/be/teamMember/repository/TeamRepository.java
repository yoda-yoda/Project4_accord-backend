package org.noteam.be.teamMember.repository;

import org.noteam.be.teamMember.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {


    Optional<Team> findById (long id);


}
