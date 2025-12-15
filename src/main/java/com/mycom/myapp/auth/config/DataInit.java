package com.mycom.myapp.auth.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.admin.Category;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.admin.repository.CategoryRepository;
import com.mycom.myapp.domain.enums.UserRole;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository; // ★ [추가] 카테고리 저장소 주입
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        // 1. 관리자 계정 생성 (기존 코드 유지)
        if (!userRepository.existsByUsername("admin")) {
            System.out.println(">>> [DataInit] 관리자(admin) 계정을 생성합니다.");
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("관리자")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
        
        // 2. 테스트용 일반 유저 (기존 코드 유지)
        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("일반유저")
                    .role(UserRole.ROLE_USER)
                    .build();
            userRepository.save(user);
        }

        // 3. ▼▼▼ [여기 추가] 카테고리 기초 데이터 생성 ▼▼▼
        if (categoryRepository.count() == 0) {
            System.out.println(">>> [DataInit] 카테고리 데이터가 없어 기본 데이터를 생성합니다.");
            
            // Category 엔티티의 필드명(title 등)에 맞춰서 작성하세요.
            categoryRepository.save(Category.builder().title("Java").build());
            categoryRepository.save(Category.builder().title("Spring Boot").build());
            categoryRepository.save(Category.builder().title("Database (SQL)").build());
            categoryRepository.save(Category.builder().title("Network").build());
            categoryRepository.save(Category.builder().title("CS 지식").build());
        }
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲
    }
}