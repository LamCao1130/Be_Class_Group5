package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ReadingPassage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingRepository extends JpaRepository<ReadingPassage,Integer> {
    List<ReadingPassage> findByLessonId(Long lessonId);
}
