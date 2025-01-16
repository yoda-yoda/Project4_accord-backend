package org.noteam.be.system.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {

    public static class MemberAuth {

        public static final String KAKAO_PROFILE_NOT_PROVIDED = "카카오 계정의 프로필 정보를 가져올 수 없습니다.";
        public static final String UNSUPPORTED_PROVIDER_EXCEPTION = "지원하지 않는 소셜 서비스 입니다.";
        public static final String MEMBER_NOT_FOUND_EXCEPTION = "회원을 찾을 수 없습니다.";

    }

    public static class ProfileImg {
        public static final String IMAGE_FILE_TOO_LARGE_EXCEPTION = "파일의 크기가 허용치보다 큽니다.";
        public static final String IMAGE_DIMENSION_EXCEEDED_EXCEPTION = "파일의 폭 또는 높이가 초과되었습니다.";

    }
}
