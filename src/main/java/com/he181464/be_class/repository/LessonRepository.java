package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByClassRoomId(Long classRoomId);

    @Query("select count(l) from Lesson l where l.status = 1")
    Integer countActiveLessons();
}
