package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.enums.Difficulty;

public interface ExamService {

	public Exam getExamById(Long id);
    public Exam createExam(User user, int questionCount, Difficulty difficulty);

}
