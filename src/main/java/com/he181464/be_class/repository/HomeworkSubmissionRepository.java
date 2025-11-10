package com.he181464.be_class.repository;

import com.he181464.be_class.entity.HomeworkSubmissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmissions, Long> {

    @Query("SELECT h FROM HomeworkSubmissions h WHERE h.studentId.id = :studentId")
    Page<HomeworkSubmissions> findHomeworkSubmissionByStudentId(@Param("studentId") Long studentId, Pageable pageable);
}
