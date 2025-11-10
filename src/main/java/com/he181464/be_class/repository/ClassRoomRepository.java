package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    boolean existsByCode(String code);

    boolean existsClassRoomByCodeAndIdIsNot(String code,Long accountId);

    Optional<ClassRoom> findById(Long classRoomId);

    List<ClassRoom> findByTeacherIdAndStatus(Long teacherId, String status);

    Page<ClassRoom> findAll(Pageable pageable);
    ClassRoom findByCode(String code);

    //List<Long> findByTeacherIdAndStatus(Long teacherId, String status);

}
