package org.noteam.be.system.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessage {

    public static final String PLEASE_INPUT_TEAM_NAME = "팀 이름을 입력해주세요";
    public static final String CANNOT_BLANK_FILE = "파일이 비어있을 수 없습니다.";
    public static final String CANNOT_BLANK_NOTE_ID = "노트 아이디는 비어 있을 수 없습니다.";
    public static final String CANNOT_BLANK_CANVAS_ID = "캔버스 아이디는 비어 있을 수 없습니다.";

}
