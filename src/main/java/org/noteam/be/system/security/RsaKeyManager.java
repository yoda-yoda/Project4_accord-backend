package org.noteam.be.system.security;

import io.jsonwebtoken.lang.Strings;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
//import org.noteam.be.grpc.client.KeyRotationNotifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class RsaKeyManager {
    // 인메모리로 읽기/쓰기 진행
    //직전 키
    @Getter
    private volatile KeyHolder previousKey;
    // 현재 키
    @Getter
    private volatile KeyHolder currentKey;

//    @Autowired
//    private KeyRotationNotifyClient keyRotationNotifyClient;

    @PostConstruct
    public void init() {
        //서버 최초 구동시 KeyPair 생성
        this.currentKey = generateNewKey();
        this.previousKey = null;
        log.info("초기 RSA 키 생성 완료. Key: {}", this.currentKey.getKid());
    }

    public PublicKey getPublicKeyByKid(String kid) {
        log.debug("키 조회 시도 - KID: {}", kid);
        if (Strings.hasText(kid)) {
            if (currentKey != null && kid.equals(currentKey.getKid())) {
                return currentKey.getPublicKey();
            }
            if (previousKey != null && kid.equals(previousKey.getKid())) {
                return previousKey.getPublicKey();
            }
        }
        log.error("키 조회 실패 - KID: {}", kid);
        return null;
    }

    private KeyHolder generateNewKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String kid = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();

            return new KeyHolder(kid, keyPair.getPrivate(), keyPair.getPublic(), now);
        } catch (Exception e) {
            throw new RuntimeException("RSA KeyPair 생성 실패", e);
        }
    }

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000) // 7일마다 실행
    public void rotateKeys() {
        synchronized (this) {
            String oldKid = (previousKey != null) ? previousKey.getKid() : "none";
            this.previousKey = this.currentKey;
            this.currentKey = generateNewKey();
            String newKid = this.currentKey.getKid();

            log.info("키 롤링 완료 - 이전 KID:{}, 새 KID:{}", oldKid, newKid);

            String pem = convertPublicKeyToPEM(this.currentKey.getPublicKey());

//            keyRotationNotifyClient.notifyKeyRolled(oldKid, newKid, pem);
        }
    }

    private String convertPublicKeyToPEM(PublicKey publicKey) {
        try (StringWriter sw = new StringWriter();
             JcaPEMWriter pemWriter = new JcaPEMWriter(sw)) {
            pemWriter.writeObject(publicKey);
            pemWriter.flush();
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("공개키를 PEM 형식으로 변환 중 오류 발생", e);
        }
    }

    @Getter
    public static class KeyHolder {
        private final String kid;
        private final PrivateKey privateKey;
        private final PublicKey publicKey;
        private final LocalDateTime createdAt;

        public KeyHolder(String kid, PrivateKey privateKey, PublicKey publicKey, LocalDateTime createdAt) {
            this.kid = kid;
            this.privateKey = privateKey;
            this.publicKey = publicKey;
            this.createdAt = createdAt;
        }
    }

}