package com.mycom.myapp.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	@GetMapping("/login")
	public String login() {
		return "forward:/login.html";
	}

	@GetMapping("/register")
	public String register() {
		return "forward:/register.html";
	}

    @GetMapping("/exam-form")
    public String examForm() {
        return "forward:/exam-form.html";
    }

	@GetMapping("/admin")
	public String admin() {
		return "forward:/admin.html"; // This is still a Thymeleaf template
	}
}
