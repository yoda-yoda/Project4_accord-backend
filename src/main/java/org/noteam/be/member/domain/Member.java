package org.noteam.be.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.noteam.be.profileimg.entity.ProfileImg;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.kanbanBoard.domain.KanbanBoardCard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;// MEMBER, ADMIN

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // ACTIVE, INACTIVE, DELETED, BANNED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 10)
    private String provider;

    // ProfileImg와의 @OneToOne 관계 추가
    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImg profileImg;


    @OneToMany
    private List<KanbanBoardCard> kanbanBoardCards = new ArrayList<>();

    // JoinBoard(팀 구인게시판)과의 @OneToOne 관계 추가 + cascade 설정 추가
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private JoinBoard joinBoard;


    public static Member of(String email,
                            String nickname,
                            Role role,
                            Status status,
                            String provider) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .role(role)
                .status(status)
                .provider(provider)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Builder
    private Member(String email,
                   String nickname,
                   Role role,
                   Status status,
                   String provider,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt,
                   ProfileImg profileImg) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileImg = profileImg;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    // 닉네임 변경용 도메인 메서드
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 변경용 도메인 메서드
    public void changeStatus(Status newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // 권한 변경용 도메인 메서드
    public void changeRole(Role newRole) {
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }

}