package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.Question;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TestQuestionRepositoryImpl implements TestQuestionRepository{
    @Override
    public List<Question> pickRandomQuestion(int questionCount) {

        return new ArrayList<>();
    }
}
