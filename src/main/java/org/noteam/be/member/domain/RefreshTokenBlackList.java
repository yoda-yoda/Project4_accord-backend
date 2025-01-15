package org.noteam.be.member.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


// 로그아웃된(무효화된) Access Token 혹은 Refresh Token을 저장
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshTokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now();

    // 무효화된 RefreshToken
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;

    @Builder
    public RefreshTokenBlackList(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

}