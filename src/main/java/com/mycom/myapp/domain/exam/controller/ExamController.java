package com.mycom.myapp.domain.exam.controller;


import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.auth.service.UserService;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamService;
import com.mycom.myapp.domain.exam.dto.ExamMakeRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j

public class ExamController {


    private final ExamService examService;

    private final UserService mockUserService;
    private final UserRepository userRepository;


    @GetMapping("/exams")
    public String Form(Model model){

        return "exam-form";
    }

    @PostMapping("/exams")
    public String makeExam(
            @AuthenticationPrincipal User user,
            ExamMakeRequestDto examMakeRequestDto,
            RedirectAttributes redirectAttributes){
        log.debug("바인딩된 DTO= {}", examMakeRequestDto);
        User mockUser = mockUserService.findUserById(1L);
        Exam exam = examService.createExam(mockUser, examMakeRequestDto.getQuestionCount(), examMakeRequestDto.getDifficulty());
        redirectAttributes.addAttribute("examId",exam.getId());
        return "redirect:/api/user/exams/{examId}";
    }

    @GetMapping("/exams/{examId}")
    public String startExam(@PathVariable("examId") Long id, Model model){
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam",exam);

        log.debug("생성된 Exam = {} ", exam);

        return "started-exam";
    }


}
