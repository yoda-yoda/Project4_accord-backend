package org.noteam.be.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
                   LocalDateTime updatedAt) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Role
    public enum Role {
        MEMBER, ADMIN
    }

    // Status (활성계정, 휴면계정, 삭제계정, 차단계정)
    public enum Status {
        ACTIVE, INACTIVE, DELETED, BANNED
    }

}