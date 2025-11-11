package com.he181464.be_class.repository;

import com.he181464.be_class.entity.SubmissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubmissionHistoryRepository extends JpaRepository<SubmissionHistory,Long> {
    @Query(value = """
        SELECT sh.*
        FROM submission_history sh
        JOIN (
            SELECT student_id, type, MAX(submitted_at) AS latest_time
            FROM submission_history
            WHERE lesson_id = :lessonId
            GROUP BY student_id, type
        ) latest 
        ON latest.student_id = sh.student_id
           AND latest.type = sh.type
           AND latest.latest_time = sh.submitted_at
        WHERE sh.lesson_id = :lessonId
        
        """, nativeQuery = true)
    List<SubmissionHistory> findLatestSubmissionPerStudentPerType(@Param("lessonId") Long lessonId);

    List<SubmissionHistory>findByLessonId(Long id);
}
