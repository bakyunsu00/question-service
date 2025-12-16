package com.mycom.myapp.domain.admin.repository;

import java.util.List;

// ▼▼▼ 이 두 줄이 꼭 있어야 합니다! ▼▼▼
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.myapp.domain.question.Question;
import com.mycom.myapp.domain.enums.Difficulty; // Enum 위치에 맞게 수정

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // 1. 관리자용: 페이징 처리된 전체 조회
    Page<Question> findAll(Pageable pageable);
    
    // 2. 관리자용: 특정 카테고리의 문제 조회
    Page<Question> findByCategoryId(Long categoryId, Pageable pageable);

    // 3. 사용자용: 랜덤 출제용 전체 리스트 (Page 아님)
    List<Question> findByCategoryIdAndDifficulty(Long categoryId, Difficulty difficulty);
}