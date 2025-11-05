package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinedClassroom {
    private Long classroomId;
    private String classroomName;
    private Long studentId;
    private LocalDateTime joinedDate;
    private String teacherName;
}
