package org.noteam.be.s3Uploader.test;

import jakarta.persistence.*;
import lombok.Getter;
import org.noteam.be.s3Uploader.entity.ProfileImg;
import software.amazon.awssdk.identity.spi.Identity;

import java.util.IdentityHashMap;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(name = "nickname")
    private String nickname;

    @OneToOne(fetch = FetchType.EAGER)
    private ProfileImg profileImg;

}
