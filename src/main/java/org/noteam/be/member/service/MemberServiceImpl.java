package org.noteam.be.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.member.domain.Member;
import org.noteam.be.member.domain.Role;
import org.noteam.be.member.domain.Status;
import org.noteam.be.member.dto.MemberProfileResponse;
import org.noteam.be.member.dto.NicknameUpdateRequest;
import org.noteam.be.member.repository.MemberRepository;
import org.noteam.be.member.dto.CustomUserDetails;
import org.noteam.be.member.dto.OAuthSignUpRequest;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.member.*;
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

        // nickname 추출
        String nickname = extractNickname(oAuth2User, registrationId);

        // DB 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member;
        if (optionalMember.isEmpty()) {
            // 가입되지 않은 경우 -> registerOAuthMember
            // 가입시 status는 active 고정
            OAuthSignUpRequest request = OAuthSignUpRequest.builder()
                    .email(email)
                    .nickname(nickname)
                    .status(Status.ACTIVE)
                    .build();

            member = registerOAuthMember(request, registrationId);
            log.info(">[OAuth2] 신규 회원 가입 완료 : {}, ({}계정)", email, registrationId);
        } else {
            member = optionalMember.get();

            // 로그인 시도 시 상태가 DELETED / BANNED 예외 발생
            if (member.getStatus() == Status.DELETED) {
                throw new DeletedAccountException(ExceptionMessage.MemberAuth.DELETED_ACCOUNT_EXCEPTION);
            }
            if (member.getStatus() == Status.BANNED) {
                throw new BannedAccountException(ExceptionMessage.MemberAuth.BANNED_ACCOUNT_EXCEPTION);
            }
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

    @Override
    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()->new MemberNotFound(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND_EXCEPTION));
    }

    // provider별로 email 추출
    // kakao는 받아온 nickname@kakao.com 형식으로 임시 설정. 추후 변경해야함.
    @Override
    public String extractEmail(OAuth2User oAuth2User, String registrationId) {
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

    // 가입 시 provider 별로 제공된 정보로 닉네임 자동 설정
    // 중복될 경우 닉네임 뒤 랜덤 4자리 숫자 제공
    @Override
    public String extractNickname(OAuth2User oAuth2User, String registrationId) {
        String baseNickname;
        switch (registrationId.toLowerCase()) {
            case "google":
                baseNickname = oAuth2User.getAttribute("name");
                break;
            case "naver":
                Map<String, String> response = (Map<String, String>) oAuth2User.getAttribute("response");
                baseNickname = response.get("name");
                break;
            case "kakao":
                Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttribute("properties");
                baseNickname = properties != null ? (String) properties.get("nickname") : null;
                break;
            default:
                throw new UnsupportedProviderException(ExceptionMessage.MemberAuth.UNSUPPORTED_PROVIDER_EXCEPTION);
        }

        if (baseNickname == null || baseNickname.trim().isEmpty()) {
            baseNickname = "User" + System.currentTimeMillis();
        }

        return generateUniqueNickname(baseNickname);
    }

    // 유니크 닉네임 생성 메서드
    @Override
    public String generateUniqueNickname(String baseNickname) {
        // 기본 닉네임으로 먼저 검색
        if (!memberRepository.existsByNickname(baseNickname)) {
            return baseNickname;
        }

        // 중복되는 경우 랜덤 숫자 4자리 추가
        while (true) {
            int randomNum = (int) (Math.random() * 9000) + 1000; // 1000-9999 범위의 숫자
            String newNickname = baseNickname + randomNum;
            if (!memberRepository.existsByNickname(newNickname)) {
                return newNickname;
            }
        }
    }

    // 닉네임 업데이트 테스트
    @Override
    @Transactional
    public MemberProfileResponse updateNickname(Long memberId, NicknameUpdateRequest request) {
        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound(ExceptionMessage.MemberAuth.MEMBER_NOT_FOUND));

        // 새 닉네임이 중복인지 확인
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new NicknameAlreadyExist(ExceptionMessage.MemberAuth.NICKNAME_ALREADY_EXIST);
        }

        // 변경 적용 (더티체킹) 동작 해서 저장됨.
        member.changeNickname(request.getNickname());

        // 응답 DTO 구성 후 반환
        return new MemberProfileResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname()
        );
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {

        // Member 조회
        Member member = this.findMemberByMemberId(memberId);

        // 삭제상태로 status 변경.(Soft Delete)
        member.changeStatus(Status.DELETED);
        log.info("Member ID={} 탈퇴 처리 완료.", memberId);
    }

}