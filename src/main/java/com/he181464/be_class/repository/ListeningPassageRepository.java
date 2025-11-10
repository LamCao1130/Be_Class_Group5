package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ListeningPassage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListeningPassageRepository extends JpaRepository<ListeningPassage,Integer> {
    List<ListeningPassage> findByLessonId(Long lessonId);
}
