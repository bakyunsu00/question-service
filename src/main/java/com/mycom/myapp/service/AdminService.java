package com.mycom.myapp.service;
import org.springframework.data.domain.Page;     
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;

import com.mycom.myapp.domain.Category;
import com.mycom.myapp.domain.Choice;
import com.mycom.myapp.domain.Question;
import com.mycom.myapp.dto.CategoryRequest;
import com.mycom.myapp.dto.ChoiceRequest;
import com.mycom.myapp.dto.QuestionCreateRequest;
import com.mycom.myapp.dto.QuestionListResponse;
import com.mycom.myapp.dto.QuestionResponse;
import com.mycom.myapp.dto.QuestionUpdateRequest;
import com.mycom.myapp.repository.CategoryRepository;
import com.mycom.myapp.repository.QuestionRepository;
import org.springframework.transaction.annotation.Transactional; // 이걸로 바꾸세요!
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본은 읽기 전용
public class AdminService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    // --- [카테고리 관리] ---
    @Transactional
    public Long createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .title(request.getTitle())
                .build();
        return categoryRepository.save(category).getId();
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));
        // Dirty Checking (변경 감지) - Setter 필요. 
        // Entity에 updateTitle() 같은 메서드를 만드는 것이 더 좋음.
        // 여기서는 편의상 Setter가 없다고 가정하고 Builder로 설명이 안되니
        // Category Entity에 `public void update(String title) { this.title = title; }` 추가 권장
        // 임시: category.setTitle(request.getTitle()); 
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // --- [문제 관리] ---
    
    // 1. 문제 등록 (C)
    @Transactional
    public Long createQuestion(QuestionCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        Question question = Question.builder()
                .category(category)
                .content(request.getContent())
                .explanation(request.getExplanation())
                .type(request.getType())
                .difficulty(request.getDifficulty())
                .build();

        // 보기 추가 (객관식인 경우)
        if (request.getChoices() != null) {
            for (ChoiceRequest c : request.getChoices()) {
                question.addChoice(Choice.builder()
                        .content(c.getContent())
                        .isAnswer(c.isAnswer())
                        .build());
            }
        }

        return questionRepository.save(question).getId();
    }

    // 2. 문제 리스트 조회 (R) - 페이징
    public Page<QuestionListResponse> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable)
                .map(QuestionListResponse::from); // Entity -> DTO 변환
    }

    // 3. 문제 상세 조회 (R)
    public QuestionResponse getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문제 없음"));
        return QuestionResponse.from(question);
    }

    // 4. 문제 수정 (U) - ★ 가장 까다로운 부분
    @Transactional
    public void updateQuestion(Long id, QuestionUpdateRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문제 없음"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        // 4-1. 기본 정보 수정 (Entity에 update 메서드 추가 권장)
        // question.update(category, request.getContent(), ...); 형태로 구현 권장.
        // 편의상 로직 설명: Dirty Checking이 일어남.
        
        // 4-2. 보기(Choices) 수정 로직
        // 전략: 기존 보기를 싹 비우고(clear), 새로운 보기를 채워 넣음(addAll).
        // orphanRemoval = true 덕분에 clear() 시 DB에서 기존 보기가 삭제됨.
        question.getChoices().clear(); 

        if (request.getChoices() != null) {
            for (ChoiceRequest c : request.getChoices()) {
                Choice newChoice = Choice.builder()
                        .content(c.getContent())
                        .isAnswer(c.isAnswer())
                        .build();
                question.addChoice(newChoice); // 다시 관계 맺기
            }
        }
    }

    // 5. 문제 삭제 (D)
    @Transactional
    public void deleteQuestion(Long id) {
        // CascadeType.ALL 덕분에 문제만 지우면 보기도 다 삭제됨
        questionRepository.deleteById(id);
    }
}