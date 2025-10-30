package com.he181464.be_class.repository;

import com.he181464.be_class.dto.ClassRoomStudentDTO;
import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassRoomStudentRepository extends JpaRepository<ClassRoomStudent,Long> {
    @Query("""
        SELECT new com.he181464.be_class.dto.ClassRoomStudentDTO(
            crs.studentId,
            cr.name,
            cr.title,
            t.fullName
        )
        FROM ClassRoomStudent crs
        JOIN ClassRoom cr ON crs.classRoomId = cr.id
        JOIN Account t ON cr.teacherId = t.id
        WHERE crs.studentId = :studentId
    """)
    Page<ClassRoomStudentDTO> findClassRoomByStudentId(long studentId, Pageable pageable);
}
