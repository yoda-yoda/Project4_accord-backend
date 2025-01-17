package org.noteam.be.profileimg.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.profileimg.entity.ProfileImg;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.MemberNotFoundException;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileImgServiceImpl implements ProfileImgService {


    private final StorageService storageService;
    private final ProfileImgConvert profileImgConvert;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void upload(MultipartFile sourceImage) throws IOException {

        //멤버 찾기
        Member member = memberRepository.findByMemberId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new MemberNotFoundException(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND_EXCEPTION)
        );

        //유니크 파일경로 만들기.
        String fileName =  member.getMemberId() + member.getNickname();

        byte[] file = profileImgConvert.convert(sourceImage);

        //S3에 업로드 후 URL 경로(String) 반환.
        String s3ImgUrl = storageService.upload(file, fileName);

        //프로파일 이미지 URL Entity 생성
        ProfileImg profileImg = ProfileImg.builder()
                .imageUrl(s3ImgUrl)
                .build();

        //저장
        member.setProfileImg(profileImg);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void setDefault() throws IOException {

        //현재멤버 찾기
        Member member = memberRepository.findByMemberId(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new MemberNotFoundException(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND_EXCEPTION)
        );

        //s3에서 기존 이미지 파일 삭제(옵션)
        storageService.delete(member.getProfileImg().getImageUrl());

        //프로파일 기본 이미지 URL 생성.
        ProfileImg profileImg = ProfileImg.builder()
                .imageUrl(storageService.getDefaultPath())
                .build();
        //저장
        member.setProfileImg(profileImg);
        memberRepository.save(member);
    }

}