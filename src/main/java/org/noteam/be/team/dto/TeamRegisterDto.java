package org.noteam.be.team.dto;

import lombok.Getter;
import org.noteam.be.team.domain.Team;

@Getter
public class TeamRegisterDto {

    private String teamName;

    public static Team toEntity(TeamRegisterDto dto){
             return Team.builder()
                    .teamName(dto.getTeamName())
                    .build();
    }


}
