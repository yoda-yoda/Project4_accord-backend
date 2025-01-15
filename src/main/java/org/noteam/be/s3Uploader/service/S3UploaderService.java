package org.noteam.be.s3Uploader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.s3Uploader.entity.ProfileImg;
import org.noteam.be.s3Uploader.repository.ProfileImgRepository;
import org.noteam.be.system.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3UploaderService implements ImgUploadService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    private final S3Client s3Client;

    private final ProfileImgRepository profileImgRepository;


    @Override
    @Transactional
    public void save(MultipartFile file) throws IOException {

        //유니크 파일경로 만들기.
//        String fileName =  member.getMemberId() + member.getNickname();

        //TEST 경로.
        String fileName =  "Test.jpg";

        //저장 후 URL 경로 반환.
        String imgSaveURL = imgURLUpload(file, fileName);

        //프로파일 이미지 URL Entity 생성
        ProfileImg profileImg = ProfileImg.builder()
                .imageUrl(imgSaveURL)
                .build();

        //저장
        profileImgRepository.save(profileImg);
    }

    @Override
    public void setDefault() {

        Long memberId = SecurityUtil.getCurrentMemberId();

        profileImgRepository.findByMemberId(memberId);

    }

    public String imgURLUpload(MultipartFile file, String fileName) throws IOException {
        //파일 저장단.
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(),file.getSize()));

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

}
