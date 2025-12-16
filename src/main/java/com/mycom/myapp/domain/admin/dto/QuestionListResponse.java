package com.mycom.myapp.domain.admin.dto;

import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionListResponse {
    private Long id;
    private String categoryTitle;
    private String content;
    private QuestionType type;
    private Difficulty difficulty;
    
    public static QuestionListResponse from(Question question) {
        return QuestionListResponse.builder()
                .id(question.getId())
                .categoryTitle(question.getCategory().getTitle())
                .content(question.getContent()) // 지문이 너무 길면 .substring() 처리 가능
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .build();
    }
}