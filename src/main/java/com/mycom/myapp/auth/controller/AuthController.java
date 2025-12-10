package com.mycom.myapp.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@GetMapping("/login")
	public String loginForm() {
		return "/login";
	}
	@GetMapping("/register")
	public String registerForm() {
		return "/register";
	}
	@PostMapping("/register")
    public String registerSubmit(RegisterDto dto) {
        authService.register(dto);
        return "redirect:/login";
    }
}
