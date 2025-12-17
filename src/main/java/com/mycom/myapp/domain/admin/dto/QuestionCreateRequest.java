package com.mycom.myapp.domain.admin.dto;

import java.util.List;

import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCreateRequest {
    private Long categoryId;
    private String content;
    private String explanation;
    private QuestionType type;    // OBJECTIVE, SUBJECTIVE
    private Difficulty difficulty; // LOW, MEDIUM, HIGH
    private List<ChoiceRequest> choices; // 보기 리스트
}