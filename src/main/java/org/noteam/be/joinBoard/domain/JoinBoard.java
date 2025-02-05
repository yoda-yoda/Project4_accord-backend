package org.noteam.be.joinBoard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.comment.domain.Comment;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // 멤버와의 @ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    // Comment(조인보드 내의 댓글)과의 @OneToMany 관계 추가
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "joinBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();




    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String projectBio;

    @Column(nullable = false)
    private String teamBio;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int peopleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // DELETE 또는 ACTIVE

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
    public void updateFromDto(JoinBoardUpdateRequest dto) {
        this.title = dto.getTitle();
        this.topic = dto.getTopic();
        this.teamName = dto.getTeamName();
        this.projectBio = dto.getProjectBio();
        this.teamBio = dto.getTeamBio();
        this.content = dto.getContent();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.peopleNumber = dto.getPeopleNumber();
    }




    @Builder
    public JoinBoard(String title, String topic, String teamName, String projectBio, String teamBio, String content, LocalDate startDate, LocalDate endDate, int peopleNumber , Member member) {
        this.title = title;
        this.topic = topic;
        this.teamName = teamName;
        this.projectBio = projectBio;
        this.teamBio = teamBio;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNumber = peopleNumber;
        this.member = member;
    }
}
