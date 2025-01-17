package org.noteam.be.profileimg.entity;
import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Status;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImg {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id") // Member의 PK를 참조하는 외래 키
    private Member member;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Status status;

    @Builder
    public ProfileImg(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

}




