package com.mycom.myapp.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						auth -> auth
							.requestMatchers(
									"/", 
					                "/login.html",
					                "/register.html",
									"/api/auth/login",
									"/api/auth/register"
							).permitAll()
							.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.disable())
				.httpBasic(httpBasic -> httpBasic.disable())
				.formLogin(form -> form.disable())
				.logout(logout -> logout.disable())
				.build();
	}

}
