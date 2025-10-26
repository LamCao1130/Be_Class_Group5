package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

}
