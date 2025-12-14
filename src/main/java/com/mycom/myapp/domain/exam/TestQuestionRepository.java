package com.mycom.myapp.domain.exam;

import com.mycom.myapp.domain.Question;
import com.mycom.myapp.domain.enums.Difficulty;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestQuestionRepository extends JpaRepository<Question, Long> {

    @Query(
            value = "SELECT * FROM question q " + // 테이블 이름은 'question'이라고 가정
                    "WHERE q.difficulty = :difficulty " +
                    "ORDER BY RAND() " +
                    "LIMIT :questionCount",
            nativeQuery = true // 💡 Native SQL 사용 명시
    )
    public List<Question> pickRandomQuestion(@Param("questionCount") int questionCount, @Param("difficulty") Difficulty difficulty);

}
