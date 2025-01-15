package org.noteam.be.system.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsConfig {


    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region)) //리전설정
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey) // 접속키, 암호키 입력.
                ))
                .build();
    }


    //Test 병합시 삭제할 것.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                auth ->
                        auth

                                .requestMatchers("/css/**", "/js/**", "/images/**","api/**").permitAll()
                                .requestMatchers("/login", "/sign-up", "/oauth2/additional-info", "/update").permitAll()
                                .requestMatchers("/member/forgot-password","/aboutUs").permitAll()
                                .requestMatchers("/member/profile").authenticated()
                                .requestMatchers("/users/**").hasAnyAuthority("MEMBER", "ADMIN")
                                .requestMatchers("/board/**").hasAuthority("ADMIN")
                                .requestMatchers("/member/**","/swipe/**","/post/**","/chat/**").permitAll()
                                .anyRequest().permitAll()
        ).build();
    }

}
