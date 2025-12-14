package com.mycom.myapp.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
// 배포시 삭제 예정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
//                        어떤 요청이든 인증 없이 접근을 허용
                        .anyRequest().permitAll()
                )
                // 폼 로그인 페이지와 관련 설정 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 기능 비활성화 (개발/테스트에 편리, 운영 환경에서는 재활성화 필요)
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }


}