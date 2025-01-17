package org.noteam.be.team.domain;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.teamMember.domain.TeamMember;

import javax.swing.text.StyleContext;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Setter
    @Column(unique = true, nullable = false, length = 15)
    private String teamName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

//     // 아직 팀멤버 도메인이 없어서 주석처리했다.
     @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
     private List<TeamMember> teamMembers;


    public void delete(boolean deleted) {
        this.deleted = deleted;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = false;
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }


}
