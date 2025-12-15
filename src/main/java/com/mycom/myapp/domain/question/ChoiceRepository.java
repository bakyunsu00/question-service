package com.mycom.myapp.domain.question;

import com.mycom.myapp.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {

}
