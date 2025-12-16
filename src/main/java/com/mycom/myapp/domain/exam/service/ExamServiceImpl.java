package com.mycom.myapp.domain.exam.service;

import com.mycom.myapp.auth.service.UserService;
import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamRecord;
import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.admin.User;
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
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional(readOnly = true)
    public Exam getExamForView(Long examId) {
        return examRepository.findByIdWithRecords(examId)
                .orElseThrow(() -> new IllegalArgumentException("시험 없음"));
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

        for (Question question : questions) {
            ExamRecord examRecord = new ExamRecord(question);
            exam.addExamRecords(examRecord);
        }
    }

    @Override
    @Transactional
    public Exam submitExam(Long examId, java.util.Map<Long, String> answers) {
        Exam exam = examRepository.findByIdWithRecords(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험지를 찾을 수 없습니다."));

        int totalScore = 0;
        int correctCount = 0;

        for (ExamRecord record : exam.getExamRecords()) {
            String userAnswer = answers.get(record.getId());
            record.setUserAnswer(userAnswer);

            // 채점 로직
            boolean isCorrect = checkAnswer(record.getQuestion(), userAnswer);
            record.setCorrect(isCorrect);

            if (isCorrect) {
                correctCount++;
            }
        }

        // 총점 계산 (정답 개수 * 100 / 전체 문제 수)
        totalScore = (int) ((double) correctCount / exam.getExamRecords().size() * 100);
        exam.setTotalScore(totalScore);

        return examRepository.save(exam);
    }

    private boolean checkAnswer(Question question, String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return false;
        }

        // 객관식인 경우 Choice에서 정답 확인
        for (com.mycom.myapp.domain.question.Choice choice : question.getChoices()) {
            if (choice.isAnswer() && choice.getContent().trim().equalsIgnoreCase(userAnswer.trim())) {
                return true;
            }
        }

        return false;
    }
}
