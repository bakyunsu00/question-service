package com.mycom.myapp.domain.exam.dto;

import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionResponseDto {
    private Long id;
    private Long recordId;
    private String content;
    private QuestionType type;
    private Difficulty difficulty;
    private List<ChoiceResponseDto> choices;
}
