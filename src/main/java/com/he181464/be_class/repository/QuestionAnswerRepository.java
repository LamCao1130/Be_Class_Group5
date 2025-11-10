package com.he181464.be_class.repository;

import com.he181464.be_class.entity.QuestionAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswers,Integer> {
    @Query("SELECT qa FROM QuestionAnswers qa " +
            "WHERE qa.submissionHistory.id = :submissionHistoryId AND qa.result <> '1'")
    List<QuestionAnswers> getIncorrectOrUnscoredAnswers(@Param("submissionHistoryId") Long id);
}
