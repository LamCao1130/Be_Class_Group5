package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    boolean existsByCode(String code);

    ClassRoom findClassRoomById(Long id);
    List<ClassRoom> findByTeacherIdAndStatus(Long teacherId, String status);

    @Query("select count(cl) from ClassRoom cl where cl.status = '1'")
    Integer countTotalActiveClassRoom();
    ClassRoom findByCode(String code);

    //List<Long> findByTeacherIdAndStatus(Long teacherId, String status);

    @Query("SELECT month(c.createdDate) as month, count(c) as total" +
            " from ClassRoom c " +
            "where year(c.createdDate) = :year " +
            "group by month(c.createdDate) " +
            "order by month(c.createdDate)")
    List<Object[]> getClassRoomByYear(@Param("year") int year);
}
