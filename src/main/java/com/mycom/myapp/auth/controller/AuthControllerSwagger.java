package com.mycom.myapp.auth.controller;

import org.springframework.http.ResponseEntity;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.RegisterDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="로그인/회원가입 REST API",description = "사용자의 로그인/회원가입 기능을 REST API로 제공")
public interface AuthControllerSwagger {
	@Operation(summary = "로그인", description = "사용자 로그인")
	public ResponseEntity<?> login(LoginRequestDto dto);
	@Operation(summary = "회원가입", description = "신규 사용자 회원가입")
	public ResponseEntity<?> registerForm(RegisterDto dto);
	@Operation(summary = "로그아웃", description = "사용자 로그아웃")
	public ResponseEntity<?> logout();
	@Operation(summary = "중복확인", description = "아이디 중복확인")
	public ResponseEntity<?> checkUsername(String username);
}
