package com.he181464.be_class.repository;

import com.he181464.be_class.entity.SubmissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionHistoryRepository extends JpaRepository<SubmissionHistory,Long> {
    List<SubmissionHistory>findByLessonId(Long id);
}
