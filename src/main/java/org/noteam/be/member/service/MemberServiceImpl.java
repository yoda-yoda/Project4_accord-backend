package org.noteam.be.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.OAuthSignUpRequest;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.KakaoProfileNotProvided;
import org.noteam.be.system.exception.member.UnsupportedProviderException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl extends DefaultOAuth2UserService implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // DefaultOAuth2UserService 통해 소셜에서 User 정보 획득
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // provider(google, naver, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toLowerCase();

        // email 추출
        String email = extractEmail(oAuth2User, registrationId);

        // DB 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member;
        if (optionalMember.isEmpty()) {
            // 가입되지 않은 경우 -> registerOAuthMember
            // nickname, status는 일단 "Pending", ACTIVE
            OAuthSignUpRequest request = OAuthSignUpRequest.builder()
                    .email(email)
                    .nickname("Pending")
                    .status(Status.ACTIVE)
                    .build();

            member = registerOAuthMember(request, registrationId);
            log.info(">[OAuth2] 신규 회원 가입 완료 : {}, ({}계정)", email, registrationId);
        } else {
            member = optionalMember.get();
        }

        // CustomUserDetails 생성
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .attributes(oAuth2User.getAttributes())
                .build();

        return customUserDetails;
    }

    @Override
    public Member registerOAuthMember(OAuthSignUpRequest request, String provider) {
        // role 은 기본 MEMBER, createdAt/updatedAt은 of()에서 자동적용
        Member newMember = Member.of(
                request.getEmail(),
                request.getNickname(),
                Role.MEMBER,        // 신규가입은 MEMBER
                request.getStatus(),// 가입시 자동으로 ACTIVE
                provider
        );
        return memberRepository.save(newMember);
    }

    //provider별로 email 추출
    //kakao는 받아온 nickname@kakao.com 형식으로 임시 설정. 추후 변경해야함.
    private String extractEmail(OAuth2User oAuth2User, String registrationId) {
        switch (registrationId.toLowerCase()) {
            case "google":
                return oAuth2User.getAttribute("email");
            case "naver":
                return ((Map<String, String>) oAuth2User.getAttribute("response")).get("email");
            case "kakao": {
                Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttribute("properties");
                if (properties == null || !properties.containsKey("nickname")) {
                    throw new KakaoProfileNotProvided(ExceptionMessage.MemberAuth.KAKAO_PROFILE_NOT_PROVIDED);
                }
                String nickname = properties.get("nickname").toString()
                        .replaceAll("[^a-zA-Z0-9]", "") //띄어쓰기같은부분제거
                        .toLowerCase();
                return nickname + "@kakao.com";
            }
            default:
                throw new UnsupportedProviderException(ExceptionMessage.MemberAuth.UNSUPPORTED_PROVIDER_EXCEPTION);
        }
    }
}