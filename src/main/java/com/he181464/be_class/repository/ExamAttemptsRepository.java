package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ExamAttempts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAttemptsRepository extends JpaRepository<ExamAttempts, Integer> {

    @Query("SELECT ea FROM ExamAttempts ea WHERE ea.student.id = :studentId")
    Page<ExamAttempts> findExamAttemptsByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT ea FROM ExamAttempts ea WHERE ea.exam.classRoom.id = :classroomId")
    List<ExamAttempts> findExamAttemptsByClassroomId(@Param("classroomId")Long classroomId);

}
