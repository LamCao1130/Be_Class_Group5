package com.he181464.be_class.repository;

import com.he181464.be_class.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType,Integer> {
    List<QuestionType> findByLessonId(Long lessonId);
}
