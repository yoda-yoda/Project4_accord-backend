package org.noteam.be.system.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt") //application.yml 에 정의
@RequiredArgsConstructor
public class JwtConfiguration {

    private final Secret secret = new Secret();
    private final Validation validation = new Validation();

    @Getter
    public static class Secret {

        private String appKey;

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }
    }

    @Getter
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