package org.noteam.be.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //팀이름
    @Column(unique = true, nullable = false, length = 15)
    private String teamName;

    //작성일
    private LocalDateTime createdAt = LocalDateTime.now();

    //수정일
    private LocalDateTime updatedAt;

    //삭제값
    private boolean deleted = false;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers;

}


// 팀 생성 로직
// user 팀을 만드는 로직 실행
// team1을 만들어
// team 1이 생성됨 이 로직에서



// 팀 삭제
// 팀 편집

