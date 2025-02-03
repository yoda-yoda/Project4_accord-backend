package org.noteam.be.admin.controller;

import lombok.RequiredArgsConstructor;
import org.noteam.be.admin.dto.MemberSearchResponse;
import org.noteam.be.admin.dto.MemberStatusUpdateRequest;
import org.noteam.be.admin.service.AdminMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    // 회원조회 api
    @GetMapping
    public ResponseEntity<Page<MemberSearchResponse>> searchMembers(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable
        ) {
        return ResponseEntity.ok(adminMemberService.searchMembers(keyword, pageable));
    }

    // 회원 상태변경 api
    @PutMapping("/{memberId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long memberId,
            @RequestBody MemberStatusUpdateRequest request
        ) {
        adminMemberService.updateMemberStatus(memberId, request.getStatus());
        return ResponseEntity.ok().build();
    }


}
