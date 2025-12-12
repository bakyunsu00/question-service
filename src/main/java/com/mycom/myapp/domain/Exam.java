package com.mycom.myapp.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 응시자

    private int totalScore; // 총점 (채점 후 업데이트)

    private LocalDateTime createdAt; // 응시 일시

    // 시험지 1장에는 여러 문제의 기록이 담김
    @Builder.Default
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamRecord> examRecords = new ArrayList<>();

    // 시험지 생성 시 날짜 자동 저장
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void setTotalScore(int score) {
        this.totalScore = score;
    }
}