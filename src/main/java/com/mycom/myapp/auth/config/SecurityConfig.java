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
import com.mycom.myapp.domain.enums.UserRole;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtUtil jwtUtil;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		JwtAuthenticationFilter jwtfFilter = new JwtAuthenticationFilter(jwtUtil);
		
		return http
				.authorizeHttpRequests(
						auth -> auth
							.requestMatchers(
									"/",
									"index.html",
					                "/login",
					                "/login.html",
					                "/register",
					                "/register.html",
									"/api/auth/**",
                                    "/api/user/**"

							).permitAll()
							.requestMatchers("/admin","/admin.html","/api/admin/**").hasRole(UserRole.ROLE_ADMIN.name().replace("ROLE_", "")) //or enum 파일에서 ROLE_ 제거
                                .requestMatchers("/api/user/**").hasRole(UserRole.ROLE_USER.name().replace("ROLE_",""))
						)
				.csrf(csrf -> csrf.disable())
				.httpBasic(httpBasic -> httpBasic.disable())
				.formLogin(form -> form.disable())
				.logout(logout -> logout.disable())

                .addFilterBefore(jwtfFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
	}

}
