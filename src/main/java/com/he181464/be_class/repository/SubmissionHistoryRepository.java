package com.he181464.be_class.repository;

import com.he181464.be_class.entity.SubmissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubmissionHistoryRepository extends JpaRepository<SubmissionHistory,Long> {
    List<SubmissionHistory>findByLessonId(Long id);
}
