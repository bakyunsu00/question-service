package com.mycom.myapp.domain.exam.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamRepository;
import com.mycom.myapp.domain.exam.dto.ExamResponseDto;
import com.mycom.myapp.domain.exam.service.ExamServiceImpl;
import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.question.TestQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private TestQuestionRepository questionRepository;

    @InjectMocks
    private ExamServiceImpl examService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        // User 엔티티에 ID 설정이 필요할 수 있습니다 (AccessDenied 체크용)
        // ReflectionTestUtils.setField(testUser, "id", 1L); 
    }

    @Test
    @DisplayName("시험지 생성 성공: 요청한 문제 수만큼 레코드가 생성되어야 한다")
    void createExam_Success() {
        // given
        int questionCount = 3;
        Difficulty difficulty = Difficulty.MEDIUM;
        List<Question> mockQuestions = List.of(new Question(), new Question(), new Question());

        when(questionRepository.pickRandomQuestion(eq(difficulty), any(Pageable.class)))
                .thenReturn(mockQuestions);

        // save 시 인자로 받은 exam 객체를 그대로 반환하도록 설정
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        ExamResponseDto response = examService.createExam(testUser, questionCount, difficulty);

        // then
        assertNotNull(response);
        assertEquals(questionCount, response.getQuestions().size());
        verify(questionRepository, times(1)).pickRandomQuestion(eq(difficulty), any(Pageable.class));
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    @DisplayName("시험지 조회 실패: 존재하지 않는 ID로 조회 시 예외가 발생한다")
    void getExamById_ThrowsException_WhenNotFound() {
        // given
        Long invalidId = 999L;
        when(examRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            examService.getExamById(invalidId);
        });
    }




}