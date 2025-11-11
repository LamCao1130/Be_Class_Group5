package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.he181464.be_class.dto.ClassRoomStudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassRoomStudentRepository extends JpaRepository<ClassRoomStudent,Long> {

    @Query("select crs.classRoomId,count(crs.studentId) " +
            "from ClassRoomStudent crs " +
            "where crs.classRoomId IN :classRoomIds " +
            "GROUP BY crs.classRoomId")
    List<Object[]> countStudentByListClassRoomId(@Param("classRoomIds") List<Long> classRoomIds);

    @Query("select count(crt.studentId) " +
            "from ClassRoomStudent crt " +
            "where crt.classRoomId = :classRoomId ")
    Integer countStudentByClassRoomId(@Param("classRoomId") Long classRoomId);

    @Query("""
        SELECT new com.he181464.be_class.dto.ClassRoomStudentDTO(
            crs.studentId,
                crs.classRoomId,
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
    Long countClassRoomStudentByStudentId(Long studentId);

    Page<ClassRoomStudent> findClassRoomStudentByStudentId(Long studentId, Pageable pageable);

    List<ClassRoomStudent> findByClassRoomId(Long classRoomId);

    ClassRoomStudent findByClassRoomIdAndStudentId(Long classRoomId, Long studentId);

    List<ClassRoomStudent> findByClassRoomId(Long classRoomId);
}
