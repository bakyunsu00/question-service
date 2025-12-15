package com.mycom.myapp.domain.exam.service;

import com.mycom.myapp.auth.service.UserService;
import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamRecord;
import com.mycom.myapp.domain.Question;
import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.exam.ExamRepository;
import com.mycom.myapp.domain.exam.ExamService;
import com.mycom.myapp.domain.question.TestQuestionRepository;

import com.mycom.myapp.exceptions.ExamNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j

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
    public Exam createExam(User user, int questionCount, Difficulty difficulty) {
        Exam exam = new Exam(user);
        addRecordsToExam(questionCount, exam, difficulty);
        return examRepository.save(exam);
    }


    private void addRecordsToExam(int questionCount, Exam exam, Difficulty difficulty) {
        Pageable pageable = PageRequest.of(0,questionCount);
        List<Question> questions = questionRepository.pickRandomQuestion(difficulty, pageable);
        log.debug("가져온 질문 수={}",questions.size());
        for (Question question : questions) {
            ExamRecord examRecord = new ExamRecord(question);
            exam.addExamRecords(examRecord);
        }
    }
}
