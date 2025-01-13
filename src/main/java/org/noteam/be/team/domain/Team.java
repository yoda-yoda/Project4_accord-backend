package org.noteam.be.team.domain;

import jakarta.persistence.*;
import lombok.*;
import javax.swing.text.StyleContext;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 15)
    private String teamName;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private boolean deleted = false;

    // 아직 팀멤버 도메인이 없어서 주석처리했다.
    // @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    // private List<TeamMember> teamMembers;


    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }



}
