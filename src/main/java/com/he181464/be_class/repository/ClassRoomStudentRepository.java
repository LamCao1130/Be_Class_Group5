package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassRoomStudentRepository extends CrudRepository<ClassRoomStudent, Long> {

    Long countClassRoomStudentByStudentId(Long studentId);

    Page<ClassRoomStudent> findClassRoomStudentByStudentId(Long studentId, Pageable pageable);
}
