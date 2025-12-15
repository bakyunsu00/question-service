package com.mycom.myapp.domain.exam;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.mycom.myapp.domain.Exam;
import com.mycom.myapp.domain.Question;
import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.exam.service.ExamServiceImpl;
import com.mycom.myapp.domain.user.UserService;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {


    @Mock
    private UserService userService;

    @Mock
    private TestQuestionRepository questionRepository;


    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamServiceImpl examService;

    @Test
    public void 시험지가_문제들을_포함해서_생성되어야_함(){
        //given
        User user = new User();

        Question mockQuestion1 = new Question();
        Question mockQuestion2 = new Question();
        List<Question> questions  = new ArrayList<>();
        questions.add(mockQuestion1);
        questions.add(mockQuestion2);
        given(questionRepository.pickRandomQuestion(2)).willReturn(questions);
        given(examRepository.save(any(Exam.class))).willAnswer(invocation -> {return invocation.getArgument(0);});






        //when
        Exam exam = examService.createExam(user,2);
        verify(examRepository, times(1)).save(any(Exam.class));
        //then

        assertEquals(mockQuestion1,exam.getExamRecords().get(0).getQuestion());
        assertEquals(mockQuestion2,exam.getExamRecords().get(1).getQuestion());
        assertEquals(2, exam.getExamRecords().size());


    }

}