package org.noteam.be.member.repository;

import org.noteam.be.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
//    Optional<RefreshToken> findByMemberId(Long memberId);

    @Query("""
        SELECT rf FROM RefreshToken rf
        LEFT JOIN RefreshTokenBlackList rtb ON rtb.refreshToken = rf
        WHERE rf.member.memberId = :memberId
        AND rtb.id IS NULL
        """)
    Optional<RefreshToken> findValidTokenByMemberId(Long memberId);

}
