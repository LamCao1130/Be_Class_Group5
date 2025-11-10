package com.he181464.be_class.repository;

import com.he181464.be_class.entity.QuestionAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswers,Integer> {
    @Query("SELECT qa FROM QuestionAnswers qa \" +\n" +
            "WHERE qa.submissionHistory.id = :submissionHistoryId AND qa.correctAnswer <> 1")
    List<QuestionAnswers> findByQuestionAnswerNotTrueBySubmission(@Param("submissionHistoryId") Long id);
}
