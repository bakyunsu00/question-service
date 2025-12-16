package com.mycom.myapp.domain.exam.controller;


import com.mycom.myapp.auth.config.CustomUserDetails;
import com.mycom.myapp.auth.service.UserService;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.exam.Exam;
import com.mycom.myapp.domain.exam.ExamService;
import com.mycom.myapp.domain.exam.dto.ExamMakeRequestDto;
import com.mycom.myapp.domain.exam.dto.ExamSubmitRequestDto;

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
    private final UserService userService;


    @GetMapping("/exams")
    public String Form(Model model){

        return "exam-form";
    }

    @PostMapping("/exams")
    public String makeExam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            ExamMakeRequestDto examMakeRequestDto,
            RedirectAttributes redirectAttributes){
        log.debug("바인딩된 DTO= {}", examMakeRequestDto);
        User user = (userDetails != null)
                ? userDetails.getUser()
                : userService.findUserById(1L);
        Exam exam = examService.createExam(user, examMakeRequestDto.getQuestionCount(), examMakeRequestDto.getDifficulty());
        redirectAttributes.addAttribute("examId",exam.getId());
        return "redirect:/api/user/exams/{examId}";
    }

    @GetMapping("/exams/{examId}")
    public String startExam(@PathVariable Long examId, Model model) {
        Exam exam = examService.getExamForView(examId);
        model.addAttribute("exam", exam);
        return "started-exam";
    }

    @PostMapping("/exams/{examId}/submit")
    public String submitExam(
            @PathVariable Long examId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            ExamSubmitRequestDto submitDto,
            RedirectAttributes redirectAttributes) {
        Exam exam = examService.submitExam(examId, submitDto.getAnswers());
        redirectAttributes.addAttribute("examId", examId);
        return "redirect:/api/user/exams/{examId}/result";
    }

    @GetMapping("/exams/{examId}/result")
    public String examResult(@PathVariable Long examId, Model model) {
        Exam exam = examService.getExamForView(examId);
        model.addAttribute("exam", exam);
        return "exam-result";
    }
    }



