package org.noteam.be.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.noteam.be.admin.dto.BoardResponse;
import org.noteam.be.admin.service.AdminBoardService;
import org.noteam.be.admin.service.AdminMemberService;
import org.noteam.be.joinBoard.domain.JoinBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자", description = "관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/boards")
public class AdminBoardController {
    private final AdminBoardService adminBoardService;

    // 해당글 삭제
    @Operation(summary = "게시물 삭제", description = "관리자의 게시물 삭제기능")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        adminBoardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    // 해당글 삭제+작성자 밴
    @Operation(summary = "게시물 삭제+작성자 차단", description = "관리자의 게시물 삭제+작성자 차단 기능")
    @DeleteMapping("/{boardId}/ban")
    public ResponseEntity<Void> deleteBoardAndBanUser(@PathVariable Long boardId) {
        adminBoardService.deleteBoardAndBanUser(boardId);
        return ResponseEntity.noContent().build();
    }

    // 회원이 작성한 게시글 조회
    @Operation(summary = "게시글 조회", description = "작성자 기준 작성글 조회")
    @GetMapping("/{memberId}/boards")
    public ResponseEntity<Page<BoardResponse>> getMemberBoards(
            @PathVariable Long memberId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<BoardResponse> boards = adminBoardService.getMemberBoards(memberId, pageable);
        return ResponseEntity.ok(boards);
    }
}
