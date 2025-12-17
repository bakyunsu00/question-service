package com.mycom.myapp.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamRecordResponseDto {
    private Long recordId;
    private String questionContent;
    private String userAnswer;
    private boolean isCorrect;
    private String correctAnswer;
}
