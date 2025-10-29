package com.he181464.be_class.repository;

import com.he181464.be_class.entity.GrammarPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarPointRepository extends JpaRepository<GrammarPoint, Integer> {

    List<GrammarPoint> findByLessonId(Integer lessonId);
}
