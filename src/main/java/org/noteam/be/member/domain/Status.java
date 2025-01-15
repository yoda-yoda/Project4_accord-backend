package org.noteam.be.member.domain;

// Status (활성계정, 휴면계정, 삭제계정, 차단계정)
public enum Status {
    ACTIVE,     // 활성계정
    INACTIVE,   // 휴면계정
    DELETED,    // 삭제계정
    BANNED      // 차단계정
}
