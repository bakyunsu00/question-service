package com.mycom.myapp.domain.exam;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam,Long> {


    //랜덤으로 문제를 뽑아오는 쿼리 필요



}
