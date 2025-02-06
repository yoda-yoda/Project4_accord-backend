package org.noteam.be.system.response.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static final String READ_USER = "SUCCESS - 회원 정보 조회 성공";

    // team
    public static final String READ_TEAM = "SUCCESS - 팀 정보 조회 성공";
    public static final String SAVE_TEAM = "SUCCESS - 팀 정보 저장 성공";
    public static final String UPDATE_TEAM = "SUCCESS - 팀 정보 수정 성공";
    public static final String DELETE_TEAM = "SUCCESS - 팀 정보 삭제 성공";


    // JoinBoard
    public static final String READ_JOIN_BOARD = "SUCCESS - 조인보드 정보 조회 성공";
    public static final String SAVE_JOIN_BOARD = "SUCCESS - 조인보드 정보 저장 성공";
    public static final String UPDATE_JOIN_BOARD = "SUCCESS - 조인보드 정보 수정 성공";
    public static final String DELETE_JOIN_BOARD = "SUCCESS - 조인보드 정보 삭제 성공";
    public static final String UPDATE_JOIN_BOARD_FAIL = "FAIL - 조인보드 정보 수정 실패";

    // search
    public static final String READ_SEARCH_JOIN_BOARD = "SUCCESS - 조인보드 검색 성공";

    
    public static final String SAVE_CANVAS_IMAGE = "SUCCESS - 캔버스 이미지 저장 성공";
    public static final String DELETE_CANVAS_IMAGE = "SUCCESS - 캔버스 이미지 삭제 성공";
    public static final String SAVE_NOTE_IMAGE = "SUCCESS - 노트 이미지 저장 성공";
    public static final String DELETE_NOTE_IMAGE = "SUCCESS - 노트 이미지 삭제 성공";

    // comment
    public static final String SAVE_COMMENT = "SUCCESS - 댓글 정보 저장 성공";
    public static final String READ_COMMENT = "SUCCESS - 댓글 정보 조회 성공";
    public static final String UPDATE_COMMENT = "SUCCESS - 댓글 정보 수정 성공";
    public static final String DELETE_COMMENT = "SUCCESS - 댓글 정보 삭제 성공";
    public static final String UPDATE_COMMENT_FAIL = "FAIL - 댓글 정보 수정 실패";

}
