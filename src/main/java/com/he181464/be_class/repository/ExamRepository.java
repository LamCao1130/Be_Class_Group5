package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Integer> {

    @Query("select e from Exam e " +
            "join e.classRoom cr " +
            "where cr.teacherId = :teacherId")
    List<Exam> findAllByTeacherId(@Param("teacherId") Long teacherId);
    List<Exam> findByClassRoomId( Long classRoomId);

    @Query("select e from Exam e " +
            "where e.examDate >= CURRENT_DATE and " +
            "e.examDate < :twoDaysLater  " +
            "and e.classRoom.id = :classroomId")
    List<Exam> getComingExam(@Param("classroomId") Long classroomId,
    @Param("twoDaysLater")LocalDateTime twoDayLater);
}
