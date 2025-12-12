package com.mycom.myapp.dto;
import com.mycom.myapp.domain.Choice;

import lombok.Builder;
import lombok.Getter;

@Getter
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