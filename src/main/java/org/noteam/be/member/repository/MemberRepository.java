package org.noteam.be.member.repository;

import org.noteam.be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberId(Long id);

    boolean existsByNickname(String nickname);


    List<Member> findByEmailContaining(String query);
}