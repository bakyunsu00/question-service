package com.mycom.myapp.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mycom.myapp.auth.jwt.JwtAuthenticationFilter;
import com.mycom.myapp.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtUtil jwtUtil;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil);
        
        return http
            // 1. JWT 사용을 위한 기본 설정 (CSRF, 세션 비활성화)
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 2. 경로별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // [누구나 접근 가능] - 정적 리소스, 로그인/가입 페이지, 공용 API
                .requestMatchers(
                        "/css/**", "/js/**", "/images/**", "/favicon.ico",
                        "/", "/index.html",
                        "/login", "/login.html",
                        "/register", "/register.html",
                        "/error",
                        "/admin",
                        "/admin.html",    // 페이지 껍데기는 허용 (JS에서 토큰 검사 후 쫓아냄)
                        "/api/auth/**",   // 로그인, 회원가입
                        "/api/categories", // 카테고리 목록 조회
                        "/started-exam.html",
                        "/exam-result.html",
                        "/api/user/exams/*", // 시험 진행/결과 페이지 (GET 요청)
                        "/exam-form.html", // 시험 생성 폼 HTML 파일 자체
                        "api/user/exams/*/result",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()

                    .requestMatchers(
                            "/api/user/exams/**"
                    ).authenticated()

                // 나머지 요청은 인증된 사용자만
                .anyRequest().authenticated()
            )
            
            // 3. JWT 필터 적용
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
