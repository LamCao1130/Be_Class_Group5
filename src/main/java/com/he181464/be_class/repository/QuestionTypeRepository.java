package com.he181464.be_class.repository;

import com.he181464.be_class.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType,Integer> {
    List<QuestionType> findByLessonId(Long lessonId);

    List<QuestionType> findByLessonIdAndType(Long lessonId, String type);

    @Query("SELECT DISTINCT qt.type FROM QuestionType qt WHERE qt.lesson.id = :lessonId")
    List<String> findDistinctTypesByLessonId(@Param("lessonId") Long lessonId);

    List<QuestionType>findByExamId(Integer examId);

}
