package org.noteam.be.profileimg;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.profileimg.entity.ProfileImg;
import org.noteam.be.profileimg.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ProfileImgTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StorageService storageService;

    @Test
    @DisplayName("멤버만들면 프로파일이미지 테이블 반응 테스트")
    void memberAndProfileImgTest() throws Exception{

        // given
        Member member = Member.of("tlgus7777@naver.com", "philo", Role.MEMBER, Status.ACTIVE, "naver");

        member = memberRepository.save(member);

        ProfileImg memberProfileImg = ProfileImg.builder()
                .imageUrl(storageService.getDefaultPath())
                .member(member)
                .build();

        // when
        member.setProfileImg(memberProfileImg);
        member = memberRepository.save(member);

        // then
        assertThat(member.getProfileImg().getImageUrl()).isEqualTo(memberProfileImg.getImageUrl());

    }

}
