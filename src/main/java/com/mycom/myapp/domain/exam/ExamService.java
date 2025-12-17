package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.exam.dto.ExamResponseDto;
import com.mycom.myapp.domain.exam.dto.ExamResultResponseDto;

public interface ExamService {

	public Exam getExamById(Long id);
	public ExamResponseDto getExamForView(Long examId, User user);
    public ExamResponseDto createExam(User user, int questionCount, Difficulty difficulty);
    public ExamResultResponseDto submitExam(Long examId, java.util.Map<Long, String> answers, User user);
    public ExamResultResponseDto getExamResult(Long examId, User user);

}
