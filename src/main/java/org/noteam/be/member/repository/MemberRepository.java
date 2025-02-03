package org.noteam.be.member.repository;

import org.noteam.be.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberId(Long id);

    boolean existsByNickname(String nickname);

    List<Member> findByEmailContaining(String query);

    @Query("SELECT m FROM Member m " +
            "WHERE m.nickname LIKE %:keyword% " +
            "ORDER BY CASE WHEN m.nickname = :exactKeyword THEN 0 ELSE 1 END, m.nickname ASC")
    Page<Member> searchByNickname(@Param("keyword") String keyword,
                                  @Param("exactKeyword") String exactKeyword,
                                  Pageable pageable);
}