package org.noteam.be.Email.Service;

import org.noteam.be.domain.Team;
import java.util.Optional;

public interface TeamService {
    Optional<Team> findByteamId(Long id);
}
