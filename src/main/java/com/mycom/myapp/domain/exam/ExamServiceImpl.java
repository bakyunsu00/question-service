//package com.mycom.myapp.domain.exam;
//
//import com.mycom.myapp.auth.service.UserService;
//import com.mycom.myapp.domain.Exam;
//import com.mycom.myapp.domain.ExamRecord;
//import com.mycom.myapp.domain.Question;
//import com.mycom.myapp.domain.User;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//
//public class ExamServiceImpl implements ExamService{
//
//
//    private final ExamRepository examRepository;
//    private final UserService userService;
//    private final TestQuestionRepository questionRepository;
//
//    @Override
//    public Exam createExam(User user, int questionCount) {
//        Exam exam = new Exam(user);
//        //question 레포지토리로 교체 필요
//        List<Question> questions = questionRepository.pickRandomQuestion(questionCount);
//        for(Question question : questions){
//            ExamRecord examRecord = new ExamRecord(question);
//            exam.addExamRecords(examRecord);
//        }
//        examRepository.save(exam);
//
//        return exam;
//    }
//}