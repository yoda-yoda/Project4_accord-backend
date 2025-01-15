package org.noteam.be.teamMember.service;

import org.noteam.be.teamMember.domain.Team;
import java.util.Optional;

public interface TeamService {
    Optional<Team> findByteamId(Long id);
}
