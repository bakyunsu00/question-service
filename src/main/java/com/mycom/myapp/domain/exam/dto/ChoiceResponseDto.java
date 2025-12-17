package com.mycom.myapp.domain.exam.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChoiceResponseDto {
    private Long id;
    private String content;
}
