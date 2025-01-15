package org.noteam.be.member.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.domain.RefreshTokenBlackList;
import org.noteam.be.member.repository.RefreshTokenBlackListRepository;
import org.noteam.be.member.repository.RefreshTokenRepository;
import org.noteam.be.member.repository.TokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    @Override
    public RefreshToken save(Member member, String token) {
        // RefreshToken 엔티티 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .member(member)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findValidRefTokenByToken(String token) {

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(token);
        if (refreshTokenOptional.isEmpty()) {
            return Optional.empty();
        }

        RefreshToken found = refreshTokenOptional.get();

        // 블랙리스트 여부 확인
        boolean isBanned = isBannedRefToken(found);

        if (isBanned) {
            return Optional.empty();
        } else {
            return refreshTokenOptional;
        }
    }

    @Override
    public Optional<RefreshToken> findValidRefTokenByMemberId(Long memberId) {
        // 블랙리스트에 없는 RefreshToken 조회하는 JPQL 은
        return refreshTokenRepository.findValidTokenByMemberId(memberId);
    }

    @Override
    public RefreshToken appendBlackList(RefreshToken token) {
        // 블랙리스트 테이블에 추가
        refreshTokenBlackListRepository.save(
                RefreshTokenBlackList.builder()
                        .refreshToken(token)
                        .build()
        );
        return token;
    }

    public boolean isBannedRefToken(RefreshToken token) {
        return refreshTokenBlackListRepository.existsByRefreshToken(token);
    }
}