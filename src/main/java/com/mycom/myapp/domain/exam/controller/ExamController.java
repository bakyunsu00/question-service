package com.mycom.myapp.domain.exam.controller;


import com.mycom.myapp.auth.config.CustomUserDetails;
import com.mycom.myapp.auth.service.UserService;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.exam.ExamService;
import com.mycom.myapp.domain.exam.dto.ExamMakeRequestDto;
import com.mycom.myapp.domain.exam.dto.ExamResponseDto;
import com.mycom.myapp.domain.exam.dto.ExamResultResponseDto;
import com.mycom.myapp.domain.exam.dto.ExamSubmitRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ExamController {

    private final ExamService examService;
    private final UserService userService;

    // == Page Forwarding == //

    @GetMapping("/exams")
    public String examFormPage() {
        return "forward:/exam-form.html";
    }

    @GetMapping("/exams/{examId}")
    public String startExamPage() {
        return "forward:/started-exam.html";
    }

    @GetMapping("/exams/{examId}/result")
    public String examResultPage() {
        return "forward:/exam-result.html";
    }


    // == API (Data) == //

    @PostMapping("/exams")
    @ResponseBody
    public ResponseEntity<ExamResponseDto> makeExam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ExamMakeRequestDto examMakeRequestDto) {
        log.debug("바인딩된 DTO= {}", examMakeRequestDto);
        User user = userDetails.getUser();
        ExamResponseDto exam = examService.createExam(user, examMakeRequestDto.getQuestionCount(), examMakeRequestDto.getDifficulty());
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/exams/{examId}/data")
    @ResponseBody
    public ResponseEntity<ExamResponseDto> startExam(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("examId") Long examId) {
        ExamResponseDto exam = examService.getExamForView(examId, userDetails.getUser());
        return ResponseEntity.ok(exam);
    }

    @PostMapping("/exams/{examId}/submit")
    @ResponseBody
    public ResponseEntity<ExamResultResponseDto> submitExam(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("examId") Long examId,
            @RequestBody ExamSubmitRequestDto submitDto) {
        ExamResultResponseDto result = examService.submitExam(examId, submitDto.getAnswers(), userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exams/{examId}/result/data")
    @ResponseBody
    public ResponseEntity<ExamResultResponseDto> examResult(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long examId) {
        ExamResultResponseDto result = examService.getExamResult(examId, userDetails.getUser());
        return ResponseEntity.ok(result);
    }
}

