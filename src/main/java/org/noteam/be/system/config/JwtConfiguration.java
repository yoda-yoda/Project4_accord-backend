package org.noteam.be.system.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt") //application.yml 에 정의
@RequiredArgsConstructor
public class JwtConfiguration {

    private final Validation validation = new Validation();

    // 비대칭key 전략으로 변경하면서 사용안함.
//    private final Secret secret = new Secret();
//    @Getter
//    @Setter
//    public static class Secret {
//
//        private String appKey;
//
//        public void setAppKey(String appKey) {
//            this.appKey = appKey;
//        }
//    }

    @Getter
    @Setter
    public static class Validation {

        private Long access;
        private Long refresh;

        public void setAccess(Long access) {
            this.access = access;
        }

        public void setRefresh(Long refresh) {
            this.refresh = refresh;
        }
    }
}