package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.admin.Question;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ExamRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam; // 소속된 시험지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question; // 어떤 문제였는지

    private String userAnswer; // 사용자가 적은 답 (객관식이면 번호나 내용, 주관식이면 텍스트)

    private boolean isCorrect; // 채점 결과 (O/X)

    // 생성자, 빌더 등을 통해 객체 생성 시 필수값 주입


    public ExamRecord(Question question) {
        this.question = question;
    }

    public void setExam(Exam exam){
        this.exam = exam;
    }

}