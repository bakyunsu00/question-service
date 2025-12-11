package com.mycom.myapp.domain.exam.dto;

import com.mycom.myapp.domain.enums.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExamMakeRequestDto {

    int questionCount;
    Difficulty difficulty;


}
