package org.noteam.be.system.util;

import org.noteam.be.member.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
    }

}
