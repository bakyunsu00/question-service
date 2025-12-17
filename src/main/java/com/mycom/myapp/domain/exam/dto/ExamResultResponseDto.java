package com.mycom.myapp.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ExamResultResponseDto {
    private Long examId;
    private String username;
    private LocalDateTime createdAt;
    private int totalScore;
    private List<ExamRecordResponseDto> results;
}
