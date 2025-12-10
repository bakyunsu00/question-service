package com.mycom.myapp.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.LoginResponseDto;
import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.service.AuthService;
import com.mycom.myapp.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		User user = authService.login(dto);

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .role(user.getRole())
                        .build()
        );
	}
	@PostMapping("/auth/register")
	public ResponseEntity<?> registerForm(@RequestBody RegisterDto dto) {
		authService.register(dto);
        return ResponseEntity.ok("회원가입 성공");
	}
}
