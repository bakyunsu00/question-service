package com.mycom.myapp.domain.admin.dto;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.question.Choice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChoiceResponse {
    private Long id;
    private String content;
    private boolean isAnswer;

    public static ChoiceResponse from(Choice choice) {
        return ChoiceResponse.builder()
                .id(choice.getId())
                .content(choice.getContent())
                .isAnswer(choice.isAnswer())
                .build();
    }
}