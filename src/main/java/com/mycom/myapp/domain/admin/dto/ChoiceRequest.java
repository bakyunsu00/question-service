package com.mycom.myapp.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChoiceRequest {
    private String content;
    
    @JsonProperty("isAnswer")
    private boolean isAnswer;
}