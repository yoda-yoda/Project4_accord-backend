package org.noteam.be.kanbanBoard.domain;


import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.member.domain.Member;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanBoardCard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //내용
    @Setter
    @Column(nullable = false)
    private String content;

    // team id 가져오기
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Member member;

    // board id
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private KanbanBoard board;

    @Setter
    private Long priority;


    @Builder
    public KanbanBoardCard(String content, Member member, KanbanBoard board,Long priority) {
        this.content = content;
        this.member = member;
        this.board = board;
        this.priority = priority;
    }


    @Override
    public String toString() {
        return "KanbanBoardCard{" +
                "content='" + content + '\'' +
                ", member=" + member +
                '}';
    }
}
