package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabRepository extends JpaRepository<Vocabulary,Long> {
    List<Optional<Vocabulary>> findByLessonId(Long lessonId);
}
