package org.noteam.be.kanbanBoard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.team.domain.Team;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class KanbanBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Setter
    @Column(nullable = false)
    private String title;


    @Setter
    private Long priority;


    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<KanbanBoardCard> cards;

    @Builder
    public KanbanBoard(Team team, String title,Long priority) {
        this.team = team;
        this.title = title;
        this.priority = priority;
    }



}
