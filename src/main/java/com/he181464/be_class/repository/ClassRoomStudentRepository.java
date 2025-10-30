package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.repository.CrudRepository;

public interface ClassRoomStudentRepository extends CrudRepository<ClassRoomStudent, Long> {

    Long countClassRoomStudentByStudentId(Long studentId);
}
