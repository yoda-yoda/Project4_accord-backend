package org.noteam.be.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String title;
    private String teamName;
    private LocalDate startDate;
    private LocalDate endDate;
}
