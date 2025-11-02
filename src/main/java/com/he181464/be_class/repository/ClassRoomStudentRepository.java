package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRoomStudentRepository extends JpaRepository<ClassRoomStudent,Long> {

    @Query("select crs.classRoomId,count(crs.studentId) " +
            "from ClassRoomStudent crs " +
            "where crs.classRoomId IN :classRoomIds " +
            "GROUP BY crs.classRoomId")
    List<Object[]> countStudentByClassRoomId(@Param("classRoomIds") List<Long> classRoomIds);
}
