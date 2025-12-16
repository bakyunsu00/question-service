package com.mycom.myapp.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.myapp.domain.admin.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

