package org.noteam.be.profileimg.repository;

import org.noteam.be.profileimg.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {

//    //멤버아이디로 ProfileImgRepository 엔티티 추출 가능.
//    Optional<ProfileImg> findByMemberId(Long memberId);

}
