package com.mycom.myapp.dto;

import java.util.List;

import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionCreateRequest {
    private Long categoryId;
    private String content;
    private String explanation;
    private QuestionType type;    // OBJECTIVE, SUBJECTIVE
    private Difficulty difficulty; // LOW, MEDIUM, HIGH
    private List<ChoiceRequest> choices; // 보기 리스트
}