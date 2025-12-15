package com.mycom.myapp.auth.controller;

import java.util.Map;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		String token = authService.login(dto);
		return ResponseEntity.ok(Map.of("token",token));		
	}
	
	@PostMapping("/auth/register")
	public ResponseEntity<?> registerForm(@RequestBody RegisterDto dto) {
		authService.register(dto);
        return ResponseEntity.ok("회원가입 성공");
	}
	@GetMapping("/check") 
    public String checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // 토큰 없이 요청하면 여기서 걸림
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "토큰 없음 (인증 안됨 - 익명 사용자)";
        }
        
        return "현재 계정: " + auth.getName() + " / 가지고 있는 권한: " + auth.getAuthorities();
    }
}
