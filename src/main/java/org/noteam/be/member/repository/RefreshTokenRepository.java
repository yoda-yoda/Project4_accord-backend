package org.noteam.be.member.repository;

import org.noteam.be.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
//    Optional<RefreshToken> findByMemberId(Long memberId);

}
