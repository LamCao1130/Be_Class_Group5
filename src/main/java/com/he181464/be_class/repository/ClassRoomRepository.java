package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    boolean existsByCode(String code);

    List<ClassRoom> findByTeacherIdAndStatus(Long teacherId, String status);

    Optional<ClassRoom> findByCode(String code);
    //List<Long> findByTeacherIdAndStatus(Long teacherId, String status);

}
