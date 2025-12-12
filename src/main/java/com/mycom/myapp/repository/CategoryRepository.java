package com.mycom.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.myapp.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

