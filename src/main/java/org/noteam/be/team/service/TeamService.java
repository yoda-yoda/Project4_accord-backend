package org.noteam.be.team.service;

import org.noteam.be.team.domain.Team;
import org.noteam.be.team.dto.TeamRegisterRequest;
import org.noteam.be.team.dto.TeamResponse;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    public List<TeamResponse> getTeamsByMemberId(Long memberId);
    TeamResponse createTeamByDto(TeamRegisterRequest dto);
    TeamResponse getTeamByIdWithResponse(Long id);
    Team getTeamById(Long id);
    List<Team> getAllTeam();
    TeamResponse updateTeamByDto(Long id, TeamRegisterRequest dto);
    void deleteTeamById(Long id);
    Optional<Team> findById(Long id);
    void hardDeleteAll();

}
