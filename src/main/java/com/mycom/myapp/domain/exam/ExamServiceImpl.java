package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.Exam;
import com.mycom.myapp.domain.ExamRecord;
import com.mycom.myapp.domain.Question;
import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.user.UserService;
import com.mycom.myapp.exceptions.ExamNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class ExamServiceImpl implements ExamService {


    private final ExamRepository examRepository;
    private final UserService userService;

    // 추후 병합된 question 레포지토리로 교체 필요
    private final TestQuestionRepository questionRepository;

    @Override
    public Exam getExamById(Long id) {
        Optional<Exam> optionalExam = examRepository.findById(id);
        return optionalExam.orElseThrow(() -> new ExamNotFoundException(id + "번 시험지를 찾을 수 없습니다."));
    }

    @Override
    public Exam createExam(User user, int questionCount) {
        Exam exam = new Exam(user);
        addRecordsToExam(questionCount, exam);
        return examRepository.save(exam);
    }


    private void addRecordsToExam(int questionCount, Exam exam) {
        List<Question> questions = questionRepository.pickRandomQuestion(questionCount);
        for (Question question : questions) {
            ExamRecord examRecord = new ExamRecord(question);
            exam.addExamRecords(examRecord);
        }
    }
}
