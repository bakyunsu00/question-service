package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.Question;
import com.mycom.myapp.domain.enums.Difficulty;
import java.util.List;

public interface TestQuestionRepository {

    public List<Question> pickRandomQuestion(int questionCount, Difficulty difficulty);

}
