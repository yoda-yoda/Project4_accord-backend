package org.noteam.be.s3Uploader.repository;

import org.noteam.be.s3Uploader.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {

    //findById로 찾는 쿼리
    Optional<ProfileImg> findByMemberId(Long memberId);
}
