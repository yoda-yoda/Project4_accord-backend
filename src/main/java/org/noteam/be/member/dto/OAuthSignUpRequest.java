package org.noteam.be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Member.Role;
import org.noteam.be.member.domain.Member.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthSignUpRequest {

    private String email;
    private String nickname;    // ex) "Pending"
    private Status status;      // ex) ACTIVE, INACTIVE 등


    // 주어진 provider(google, naver, kakao 등)에 맞게 Member 엔티티 생성. 기본 role 은 MEMBER
    public Member toEntity(String provider) {
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .role(Role.MEMBER)
                .status(this.status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .provider(provider)
                .build();
    }
}