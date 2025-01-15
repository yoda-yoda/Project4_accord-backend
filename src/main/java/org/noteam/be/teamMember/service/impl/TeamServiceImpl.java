package org.noteam.be.teamMember.service.impl;

import lombok.RequiredArgsConstructor;
import org.noteam.be.teamMember.service.TeamService;
import org.noteam.be.teamMember.repository.TeamRepository;
import org.noteam.be.teamMember.domain.Team;
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
