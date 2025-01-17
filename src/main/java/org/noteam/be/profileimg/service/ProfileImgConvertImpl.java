package org.noteam.be.profileimg.service;

import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.profileImg.ImageValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ProfileImgConvertImpl implements ProfileImgConvert{


    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        BufferedImage sourceImage = checkValidImg(file);

        BufferedImage processDoneImage = cropAndResizeImage(sourceImage);

        return convertBufferedImageToByteArray(processDoneImage);
    }


    private BufferedImage cropAndResizeImage(BufferedImage image) {

        //이미지 원본 가로세로 추출
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        //작은 쪽 기준 1:1 크롭사이즈 추출
        int cropSize = Math.min(originalWidth, originalHeight);

        //사진 센터 따기
        int cropX = (originalWidth - cropSize) / 2;
        int cropY = (originalHeight - cropSize) / 2;

        //크롭 진행 구문(x 지점, y지점, 가로사이즈, 세로사이즈)
        BufferedImage croppedImage = image.getSubimage(cropX, cropY, cropSize, cropSize);

        //리사이징할 크기 정의
        Image resizedImage = croppedImage.getScaledInstance(MAX_WIDTH, MAX_HEIGHT, Image.SCALE_AREA_AVERAGING);

        //(가로, 세로, 이미지 타입)틀 정의 tip:TYPE_INT_RGB 는 24bit 임)
        BufferedImage outputImage = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_RGB);

        //outputImage 에 그림그릴 툴 준비.(어떤 버퍼드 이미지의 그래픽스 객체준비)
        Graphics2D g2d = outputImage.createGraphics();

        //리사이징까지 끝난 이미지를 장전함.
        g2d.drawImage(resizedImage, 0, 0, null);

        //그리기
        g2d.dispose();

        return outputImage;
    }


    protected BufferedImage checkValidImg(MultipartFile file) throws IOException {

        //최초 사이즈 체크
        if (file.getSize() > MAX_SIZE) {
            throw new ImageValidationException(ExceptionMessage.ProfileImg.IMAGE_FILE_TOO_LARGE_EXCEPTION);
        }

        //이미지 크기 편집할 수 있게끔 만들기
        BufferedImage image = ImageIO.read(file.getInputStream());

        //파일이 너무 과하게 클 경우
        if (image.getWidth() > VALID_MAX_WIDTH || image.getHeight() > VALID_MAX_HEIGHT) {
            throw new ImageValidationException(ExceptionMessage.ProfileImg.IMAGE_DIMENSION_EXCEEDED_EXCEPTION);
        }

        return image;
    }


    private byte[] convertBufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

}
