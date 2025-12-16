package com.mycom.myapp.domain.question;

import com.mycom.myapp.domain.admin.Question;
import com.mycom.myapp.domain.enums.Difficulty;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestQuestionRepository extends JpaRepository<Question, Long> {

    @Query(


            value = "SELECT q FROM Question q JOIN fetch q.choices WHERE q.difficulty = :difficulty ORDER BY function('rand')"
    )
    public List<Question> pickRandomQuestion(@Param("difficulty") Difficulty difficulty, Pageable pageable);

}
