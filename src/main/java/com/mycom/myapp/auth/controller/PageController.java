package com.mycom.myapp.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class PageController {

	@GetMapping("/login")
	public String login() {
		return "forward:/login.html";
	}

	@GetMapping("/register")
	public String register() {
		return "forward:/register.html";
	}
}
