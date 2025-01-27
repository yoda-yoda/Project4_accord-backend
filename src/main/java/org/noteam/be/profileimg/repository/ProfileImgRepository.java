package org.noteam.be.profileimg.repository;

import org.noteam.be.member.domain.Member;
import org.noteam.be.profileimg.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {

    //멤버아이디로 ProfileImgRepository 엔티티 추출 가능.
    Optional<ProfileImg> findByMemberMemberId(Long memberId);

    @Query("SELECT p.imageUrl FROM ProfileImg p WHERE p.member = :member")
    String findProfileImgByMember(Member member);
}
