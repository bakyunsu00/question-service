package com.mycom.myapp.domain.exam;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExamRepository extends JpaRepository<Exam,Long> {


    @Query("""
        select e
        from Exam e
        left join fetch e.examRecords
        left join fetch e.user
        where e.id = :id
    """)
    Optional<Exam> findByIdWithRecords(@Param("id") Long id);
}



