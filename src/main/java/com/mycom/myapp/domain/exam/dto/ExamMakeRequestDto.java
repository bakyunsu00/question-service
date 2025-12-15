package com.mycom.myapp.domain.exam.dto;

import com.mycom.myapp.domain.enums.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExamMakeRequestDto {

    private int questionCount;
    private Difficulty difficulty;


}
