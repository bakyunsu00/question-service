package com.mycom.myapp.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	@GetMapping("/login")
	public String login() {
		return "login.html";
	}
	@GetMapping("/register")
	public String register() {
		return "register.html";
	}
	@GetMapping("/admin")
	public String admin() {
		return "admin.html";
	}
}
