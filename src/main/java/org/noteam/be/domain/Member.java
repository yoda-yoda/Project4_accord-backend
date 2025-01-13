package org.noteam.be.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.noteam.be.domain.roleEnum.Role;
import org.noteam.be.domain.statusEnum.Status;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //이메일
    @Column(nullable = false, length = 20)
    private String email;

    //닉네임
    @Column(nullable = false, length = 20)
    private String nickname;

    //권한
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    //상태
    private Status status;

    //가입일
    private LocalDateTime createdAt = LocalDateTime.now();

    //프로필 수정일
    private LocalDateTime updatedAt;

    //제공자
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

}
