package com.mycom.myapp.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthController implements AuthControllerSwagger{
	
	private final AuthService authService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		log.info("[login] 로그인 시도 : username = {} ", dto.getUsername());
		String token = authService.login(dto);
		log.info("[login] 로그인 성공 : username = {} ", dto.getUsername());
		return ResponseEntity.ok(Map.of("token",token));		
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<?> registerForm(@Valid @RequestBody RegisterDto dto) {
		log.info("[register] 회원가입 시도 : username = {} ", dto.getUsername());
		authService.register(dto);
		log.info("[register] 회원가입 성공 : username = {} ", dto.getUsername());
        return ResponseEntity.ok("회원가입 성공");
	}
	
	@PostMapping("/auth/logout")
	public ResponseEntity<?> logout(){
		return ResponseEntity.ok("로그아웃 완료");
	}
	
	@GetMapping("/auth/check")
	public ResponseEntity<?> checkUsername(@RequestParam("username") String username){
		boolean available = authService.isUsernameAvailable(username);
		return ResponseEntity.ok(Map.of("available",available));
	}
}
