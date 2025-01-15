package org.noteam.be.member.repository;

import org.noteam.be.member.domain.RefreshToken;
import org.noteam.be.member.domain.RefreshTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenBlackListRepository extends JpaRepository<RefreshTokenBlackList, Long> {

    // 특정 RefreshToken이 블랙리스트에 있는지 여부
    boolean existsByRefreshToken(RefreshToken refreshToken);

//    Optional<RefreshTokenBlackList> findByTokenValue(String tokenValue);

}