package com.mycom.myapp.domain.admin.controller;

import com.mycom.myapp.domain.admin.Category;
import com.mycom.myapp.domain.admin.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories") // admin.js가 요청하는 주소와 일치!
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // 카테고리 전체 목록 조회
    @GetMapping
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}