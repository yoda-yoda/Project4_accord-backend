package org.noteam.be.team.service;

import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamRegisterRequest;
import org.noteam.be.team.dto.TeamResponse;

import java.util.List;

public interface TeamService {
    
    TeamResponse createTeamByDto(TeamRegisterRequest dto);
    TeamResponse getTeamByIdWithResponse(Long id);
    Team getTeamById(Long id);
    List<Team> getAllTeam();
    TeamResponse updateTeamByDto(Long id, TeamRegisterRequest dto);
    void deleteTeamById(Long id);


}
