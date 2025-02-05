package org.noteam.be.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.comment.dto.CommentUpdateRequest;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.member.domain.Member;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // DELETE 또는 ACTIVE


    // 조인보드와의 @ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "join_board_id", nullable = false)
    private JoinBoard joinBoard;


    // 멤버와의 @ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // 최초로 엔티티가 DB에 저장되면, 자동으로 이 메서드가 실행됨
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }



    // 기존 엔티티의 변경감지가 인식되면, 자동으로 이 메서드가 실행됨
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    // 소프트 딜리트 처리 변경을 위한 메서드
    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }


    // 기존 엔티티 내용을, 보안상 setter가 아니라 dto를 통해 수정하기위한 메서드
    public void updateFromDto(CommentUpdateRequest dto) {
        this.content = dto.getContent();
    }


    @Builder
    public Comment(String content, JoinBoard joinBoard, Member member) {
        this.content = content;
        this.joinBoard = joinBoard;
        this.member = member;
    }



}
