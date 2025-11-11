package com.he181464.be_class.repository;

import com.he181464.be_class.entity.QuestionOption;
import com.he181464.be_class.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {
    @Query("SELECT qo FROM QuestionOption qo " +
            "WHERE qo.questions.questionType.lesson.id = :lessonId")
    List<QuestionOption> findQuestionOptionByLessonId(@Param("lessonId") Long id);

    @Query("SELECT q from Questions q " +
            "WHERE q.questionType.lesson.id =:lessonId")
    List<Questions> findByQuestionType_LessonId(@Param("lessonId") Long id);

    @Query("SELECT q from Questions q " +
            "WHERE q.questionType.id =:lessonId")
    List<Questions> findByQuestionType_Id(@Param("lessonId") Integer id);

    @Query("SELECT qo FROM QuestionOption qo " +
            "WHERE qo.questions.questionType.id = :questionTypeId")
    List<QuestionOption> findQuestionOptionByQuestionTypeId(@Param("questionTypeId") Integer id);

    @Query("SELECT q from Questions q " +
            "WHERE q.questionType.exam.id =:examId")
    List<Questions> findByQuestionType_ExamId(@Param("examId") Integer id);

    @Query("SELECT qo FROM QuestionOption qo " +
            "WHERE qo.questions.questionType.exam.id = :examId")
    List<QuestionOption> findQuestionOptionByExamId(@Param("examId") Integer id);

    @Query("SELECT qo FROM QuestionOption qo " +
            "WHERE qo.questions.questionType.exam.id = :examId AND qo.correctAnswer = true")
    List<QuestionOption> findQuestionOptionCorrectByExam(@Param("examId") Integer examId);
}
