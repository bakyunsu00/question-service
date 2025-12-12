package com.mycom.myapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mycom.myapp.domain.Category;
import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.enums.UserRole;
import com.mycom.myapp.repository.CategoryRepository;
import com.mycom.myapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // 1. 카테고리 데이터가 없으면 넣기
        if (categoryRepository.count() == 0) {
            System.out.println(">>> 초기 카테고리 데이터 생성 중...");
            
            categoryRepository.save(Category.builder().title("정보처리기사").build());
            categoryRepository.save(Category.builder().title("JAVA").build());
            categoryRepository.save(Category.builder().title("네트워크").build());
            categoryRepository.save(Category.builder().title("데이터베이스").build());
        }

        // 2. 관리자 계정이 없으면 만들기 (나중에 로그인 테스트용)
        if (userRepository.count() == 0) {
            System.out.println(">>> 초기 관리자 계정 생성 중...");
            
            User admin = User.builder()
                    .username("admin")
                    .password("1234") // 나중엔 암호화해야 함
                    .nickname("관리자")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            
            userRepository.save(admin);
            
            User user = User.builder()
                    .username("user")
                    .password("1234")
                    .nickname("일반유저")
                    .role(UserRole.ROLE_USER)
                    .build();
            
            userRepository.save(user);
        }
    }
}