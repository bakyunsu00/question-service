package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.Question;
import java.util.List;

public interface TestQuestionRepository {

    public List<Question> pickRandomQuestion(int questionCount);

}
