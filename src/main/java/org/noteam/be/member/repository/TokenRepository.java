package org.noteam.be.member.repository;

import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.RefreshToken;
import java.util.Optional;

public interface TokenRepository {

    RefreshToken save(Member member, String token);

    Optional<RefreshToken> findValidRefTokenByToken(String token);

    Optional<RefreshToken> findValidRefTokenByMemberId(Long memberId);

    RefreshToken appendBlackList(RefreshToken token);

}
