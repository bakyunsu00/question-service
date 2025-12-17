package com.mycom.myapp.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExamResponseDto {
    private Long examId;
    private List<QuestionResponseDto> questions;
}
