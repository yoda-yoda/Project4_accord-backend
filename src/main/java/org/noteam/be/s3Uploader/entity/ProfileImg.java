package org.noteam.be.s3Uploader.entity;

import jakarta.persistence.*;
import lombok.*;
import org.noteam.be.s3Uploader.test.Member;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImg {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="member_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Member member;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public ProfileImg(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }
}




