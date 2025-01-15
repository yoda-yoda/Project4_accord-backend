package org.noteam.be.system.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {

    public static final String EMAIL_SENDING_ERROR = "이메일 전송 도중 오류가 생겼습니다";
    public static final String EMPTY_TEAM = "팀이 존재하지 않습니다.";

}
