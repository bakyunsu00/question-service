package com.mycom.myapp.domain.admin.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionResponse {
    private Long id;
    private String categoryTitle;
    private String content;
    private String explanation;
    private QuestionType type;
    private Difficulty difficulty;
    private List<ChoiceResponse> choices;

    // Entity -> DTO 변환 메서드 (static factory method)
    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .categoryTitle(question.getCategory().getTitle())
                .content(question.getContent())
                .explanation(question.getExplanation())
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .choices(question.getChoices().stream()
                        .map(ChoiceResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}