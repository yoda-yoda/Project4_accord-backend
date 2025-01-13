package org.noteam.be.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 10)
    private String provider;

    @Builder
    public Member(String email, String nickname, Role role, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.provider = provider;
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
