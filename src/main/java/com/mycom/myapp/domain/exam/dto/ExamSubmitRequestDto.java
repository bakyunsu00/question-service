package com.mycom.myapp.domain.exam.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ExamSubmitRequestDto {
    // key: examRecordId, value: userAnswer
    private Map<Long, String> answers;
}
