package com.mycom.myapp.domain;

import java.util.ArrayList;
import java.util.List;

import com.mycom.myapp.domain.enums.Difficulty;
import com.mycom.myapp.domain.enums.QuestionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // 긴 텍스트
    private String content; // 문제 지문

    @Lob
    private String explanation; // 해설 (오답노트용)

    @Enumerated(EnumType.STRING)
    private QuestionType type; // 객관식/주관식

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty; // 상/중/하

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 문제 하나에 보기가 여러 개 (객관식일 경우)
    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

    // 연관관계 편의 메서드 (양방향 세팅용)
    public void addChoice(Choice choice) {
        this.choices.add(choice);
        choice.setQuestion(this);
    }

    // 빌더 패턴이나 생성자 추가 권장
}