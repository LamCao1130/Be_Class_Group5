package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassRoomStudentDTO {
    private long studentId;
    private long classRoomId;
    private String name;
    private String title;
    private String teacherName;
}
