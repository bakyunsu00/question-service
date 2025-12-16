package com.mycom.myapp.domain.admin.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.domain.admin.service.AdminService;
import com.mycom.myapp.domain.admin.dto.CategoryRequest;
import com.mycom.myapp.domain.admin.dto.QuestionCreateRequest;
import com.mycom.myapp.domain.admin.dto.QuestionListResponse;
import com.mycom.myapp.domain.admin.dto.QuestionResponse;
import com.mycom.myapp.domain.admin.dto.QuestionUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')") // Security 적용 시 주석 해제
public class AdminController {

    private final AdminService adminService;

    // --- 카테고리 API ---
    @PostMapping("/categories")
    public ResponseEntity<Long> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(adminService.createCategory(request));
    }

    // --- 문제 API ---

    // 1. 문제 등록
    @PostMapping("/questions")
    public ResponseEntity<Long> createQuestion(@RequestBody QuestionCreateRequest request) {
        return ResponseEntity.ok(adminService.createQuestion(request));
    }

    // 2. 문제 리스트 조회 (페이징)
    // 사용법: GET /api/admin/questions?page=0&size=10&sort=id,desc
    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionListResponse>> getQuestions(Pageable pageable) {
        return ResponseEntity.ok(adminService.getQuestions(pageable));
    }

    // 3. 문제 상세 조회
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getQuestion(id));
    }

    // 4. 문제 수정
    @PutMapping("/questions/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdateRequest request) {
        adminService.updateQuestion(id, request);
        return ResponseEntity.ok("수정 완료");
    }

    // 5. 문제 삭제
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        adminService.deleteQuestion(id);
        return ResponseEntity.ok("삭제 완료");
    }
}