package org.noteam.be.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.team.domain.Team;

import static org.noteam.be.system.util.ValidationMessage.PLEASE_INPUT_TEAM_NAME;

@Getter
@NoArgsConstructor
public class TeamRegisterRequest {

    @NotBlank(message = PLEASE_INPUT_TEAM_NAME)
    private String teamName;

    public static Team toEntity(TeamRegisterRequest dto){
             return Team.builder()
                    .teamName(dto.getTeamName())
                    .build();
    }

    @Builder
    public TeamRegisterRequest(String teamName) {
        this.teamName = teamName;
    }
}
