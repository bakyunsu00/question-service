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
							.requestMatchers("/","/login","/register").permitAll()
							.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.disable())
				.formLogin(
						form -> form
							.loginPage("/login")
							.defaultSuccessUrl("/",true)
							.permitAll()
				)
				.logout(
					logout -> logout
							.logoutUrl("/logout")
							.logoutSuccessUrl("/login")
				)
				.build();
	}

}
