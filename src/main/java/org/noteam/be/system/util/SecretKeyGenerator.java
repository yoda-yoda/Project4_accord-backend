package org.noteam.be.system.util;

import java.security.SecureRandom;
import java.util.Base64;


public class SecretKeyGenerator {

    // 새로운 키 생성 유틸 메서드
    // 필요할때만 실행하여서 application.yml 의 환경변수에 대입시킬 필요가 있음.
    public static String generateKey() {

        SecureRandom secureRandom = new SecureRandom();

        byte[] key = new byte[32];
        secureRandom.nextBytes(key);

        return Base64.getEncoder().encodeToString(key);

    }

    /* **운영환경에서 사용**
    String newKey = SecretKeyGenerator.generateKey();
    System.out.println(newKey);
    export JWT_SECRET_KEY=생성된 키값
    */

}