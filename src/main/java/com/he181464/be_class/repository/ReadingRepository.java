package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ReadingPassage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<ReadingPassage,Integer> {
}
