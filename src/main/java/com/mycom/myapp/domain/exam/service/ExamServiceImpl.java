
package com.mycom.myapp.domain.exam.service;

import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamRecord;
import com.mycom.myapp.domain.exam.dto.*;
import com.mycom.myapp.domain.question.Choice;
import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.exam.ExamRepository;
import com.mycom.myapp.domain.exam.ExamService;
import com.mycom.myapp.domain.admin.repository.QuestionRepository;
import com.mycom.myapp.domain.question.TestQuestionRepository;
import com.mycom.myapp.exceptions.ExamNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import java.util.stream.Collectors;

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

    // 추후 병합된 question 레포지토리로 교체 필요
    private final TestQuestionRepository questionRepository;

    @Override
    public Exam getExamById(Long id) {
        Optional<Exam> optionalExam = examRepository.findById(id);
        return optionalExam.orElseThrow(() -> new ExamNotFoundException(id + "번 시험지를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public ExamResponseDto getExamForView(Long examId, User user) {
        Exam exam = examRepository.findByIdWithRecords(examId)
                .orElseThrow(() -> new IllegalArgumentException("시험 없음"));
        if (!exam.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("이 시험지를 볼 권한이 없습니다.");
        }
        return entityToExamResponseDto(exam);
    }

    @Override
    @Transactional
    public ExamResponseDto createExam(User user, int questionCount, Difficulty difficulty) {
        Exam exam = new Exam(user);
        addRecordsToExam(questionCount, exam, difficulty);
        Exam savedExam = examRepository.save(exam);
        return entityToExamResponseDto(savedExam);
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
    public ExamResultResponseDto submitExam(Long examId, java.util.Map<Long, String> answers, User user) {
        Exam exam = examRepository.findByIdWithRecords(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험지를 찾을 수 없습니다."));
        if (!exam.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("이 시험지를 제출할 권한이 없습니다.");
        }

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

        Exam savedExam = examRepository.save(exam);
        return entityToExamResultResponseDto(savedExam);
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

    @Override
    @Transactional(readOnly = true)
    public ExamResultResponseDto getExamResult(Long examId, User user) {
        Exam exam = examRepository.findByIdWithRecords(examId)
                .orElseThrow(() -> new ExamNotFoundException("시험지를 찾을 수 없습니다."));
        if (!exam.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("이 시험 결과를 볼 권한이 없습니다.");
        }
        return entityToExamResultResponseDto(exam);
    }

    private ExamResponseDto entityToExamResponseDto(Exam exam) {
        List<QuestionResponseDto> questionDtos = exam.getExamRecords().stream()
                .map(this::entityToQuestionResponseDto)
                .collect(Collectors.toList());

        return ExamResponseDto.builder()
                .examId(exam.getId())
                .questions(questionDtos)
                .build();
    }

    private QuestionResponseDto entityToQuestionResponseDto(ExamRecord record) {
        Question question = record.getQuestion();
        List<ChoiceResponseDto> choiceDtos = question.getChoices().stream()
                .map(this::entityToChoiceResponseDto)
                .collect(Collectors.toList());

        return QuestionResponseDto.builder()
                .id(question.getId())
                .recordId(record.getId())
                .content(question.getContent())
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .choices(choiceDtos)
                .build();
    }

    private ChoiceResponseDto entityToChoiceResponseDto(Choice choice) {
        return ChoiceResponseDto.builder()
                .id(choice.getId())
                .content(choice.getContent())
                .build();
    }

    private ExamResultResponseDto entityToExamResultResponseDto(Exam exam) {
        List<ExamRecordResponseDto> recordDtos = exam.getExamRecords().stream()
                .map(this::entityToExamRecordResponseDto)
                .collect(Collectors.toList());

        return ExamResultResponseDto.builder()
                .examId(exam.getId())
                .username(exam.getUser().getUsername())
                .createdAt(exam.getCreatedAt())
                .totalScore(exam.getTotalScore())
                .results(recordDtos)
                .build();
    }

    private ExamRecordResponseDto entityToExamRecordResponseDto(ExamRecord record) {
        return ExamRecordResponseDto.builder()
                .recordId(record.getId())
                .questionContent(record.getQuestion().getContent())
                .userAnswer(record.getUserAnswer())
                .isCorrect(record.isCorrect())
                .correctAnswer(getCorrectAnswer(record.getQuestion()))
                .build();
    }

    private String getCorrectAnswer(Question question) {
        return question.getChoices().stream()
                .filter(Choice::isAnswer)
                .map(Choice::getContent)
                .findFirst()
                .orElse(null);
    }
}
