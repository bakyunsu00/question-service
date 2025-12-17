package com.mycom.myapp.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycom.myapp.domain.enums.Difficulty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChoiceRequest {
    private String content;
    
    @JsonProperty("isAnswer")
    private boolean isAnswer;
}