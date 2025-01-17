package org.noteam.be.kanbanBoard.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.team.domain.Team;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private String title;

    //
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<KanbanBoardCard> cards;

    @Builder
    public KanbanBoard(Team team, String title) {
        this.team = team;
        this.title = title;
    }



}
