package com.mycom.myapp.domain.exam;


import com.mycom.myapp.domain.Exam;
import com.mycom.myapp.domain.User;

import com.mycom.myapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ExamController {


    private final ExamService examService;

    private final UserService mockUserService;


    @GetMapping("/exams")
    public String Form(){
        return "exam-form";
    }

    @PostMapping("/exams")
    public String makeExam(
            @AuthenticationPrincipal User user,
            @RequestParam int questionCount,
            RedirectAttributes redirectAttributes){
        User mockUser = mockUserService.findUserById(1L);

        Exam exam = examService.createExam(mockUser, questionCount);
        redirectAttributes.addAttribute("examId",exam.getId());
        return "redirect:/api/user/exams/{examId}";
    }

    @GetMapping("/exams/{examId}")
    public String startExam(@PathVariable("examId") Long id, Model model){
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam",exam);
        return "started-exam";
    }


}
