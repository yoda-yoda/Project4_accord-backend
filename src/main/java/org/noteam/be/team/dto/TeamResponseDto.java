package org.noteam.be.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.team.domain.Team;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamResponseDto {

    private Long id;
    private String teamName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

    @Builder
    public TeamResponseDto(Long id, String teamName, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        this.id = id;
        this.teamName = teamName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public static TeamResponseDto fromEntity(Team team) {
       return TeamResponseDto.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .deleted(team.isDeleted())
                .build();
    }


}
