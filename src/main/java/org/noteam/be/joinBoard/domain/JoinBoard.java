package org.noteam.be.joinBoard.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.joinBoard.dto.JoinBoardUpdateRequest;
import org.noteam.be.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


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
    public JoinBoard(String title, String topic, String teamName, String projectBio, String teamBio, String content, LocalDate startDate, LocalDate endDate, int peopleNumber) {
        this.title = title;
        this.topic = topic;
        this.teamName = teamName;
        this.projectBio = projectBio;
        this.teamBio = teamBio;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.peopleNumber = peopleNumber;

    }
}
