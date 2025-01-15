package org.noteam.be.Email.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.Email.Service.TeamService;
import org.noteam.be.dao.TeamRepository;
import org.noteam.be.domain.Team;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Optional<Team> findByteamId(Long id) {
      return teamRepository.findById(id);
    }



}
