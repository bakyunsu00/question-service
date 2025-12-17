package com.mycom.myapp.domain.admin.service;

import com.mycom.myapp.domain.admin.Category;
import com.mycom.myapp.domain.admin.dto.CategoryRequest;
import com.mycom.myapp.domain.admin.dto.ChoiceRequest;
import com.mycom.myapp.domain.admin.dto.QuestionCreateRequest;
import com.mycom.myapp.domain.admin.repository.CategoryRepository;
import com.mycom.myapp.domain.admin.repository.QuestionRepository;
import com.mycom.myapp.domain.enums.QuestionType;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.question.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; // ✅ 이 임포트가 있어야 실행 아이콘이 뜹니다!
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 환경 설정
class AdminServiceTest { // ✅ 클래스 이름 뒤에 Test가 붙어있음

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminService adminService; // ✅ 테스트할 원본 서비스 주입

    @Test
    @DisplayName("카테고리 생성 성공 테스트")
    void createCategoryTest() {
        // given (준비)
        CategoryRequest request = new CategoryRequest();
        request.setTitle("자바");
        Category category = Category.builder().id(1L).title("자바").build();
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when (실행)
        Long savedId = adminService.createCategory(request);

        // then (검증)
        assertThat(savedId).isEqualTo(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("객관식 문제 등록 성공 테스트")
    void createQuestionTest() {
        // given
        Category category = Category.builder().id(1L).title("자바").build();
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        QuestionCreateRequest request = new QuestionCreateRequest();
        request.setCategoryId(1L);
        request.setContent("테스트 지문");
        request.setType(QuestionType.OBJECTIVE);
        request.setDifficulty(Difficulty.MEDIUM);
        
        ChoiceRequest c = new ChoiceRequest();
        c.setContent("보기1");
        c.setAnswer(true);
        request.setChoices(List.of(c));

        Question question = Question.builder().id(100L).build();
        given(questionRepository.save(any(Question.class))).willReturn(question);

        // when
        Long savedId = adminService.createQuestion(request);

        // then
        assertThat(savedId).isEqualTo(100L);
    }

    @Test
    @DisplayName("문제 삭제 테스트")
    void deleteQuestionTest() {
        // given
        Long qId = 1L;

        // when
        adminService.deleteQuestion(qId);

        // then
        verify(questionRepository).deleteById(qId);
    }
}