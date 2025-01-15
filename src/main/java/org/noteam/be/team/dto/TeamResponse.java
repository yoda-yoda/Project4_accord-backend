package org.noteam.be.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.system.util.TimeAgoUtil;
import org.noteam.be.team.domain.Team;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {


    private Long id;
    private String teamName;
    private String createdAt;
    private String updatedAt;


    public static TeamResponse fromEntity(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .createdAt(TimeAgoUtil.formatElapsedTime(team.getCreatedAt()))
                .updatedAt(TimeAgoUtil.formatElapsedTime(team.getUpdatedAt()))
                .build();
    }









}
