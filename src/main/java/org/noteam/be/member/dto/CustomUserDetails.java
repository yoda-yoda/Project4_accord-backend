package org.noteam.be.member.dto;


import lombok.Builder;
import lombok.Getter;
import org.noteam.be.member.domain.Member.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements OAuth2User {

    private final Long memberId;
    private final String email;
    private final String nickname;
    private final Role role;
    private final Map<String, Object> attributes;

    @Builder
    public CustomUserDetails(Long memberId, String email, String nickname, Role role, Map<String, Object> attributes) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getName() {
        return email != null ? email : String.valueOf(memberId);
    }

}
