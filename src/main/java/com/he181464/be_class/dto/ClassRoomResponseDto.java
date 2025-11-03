package com.he181464.be_class.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClassRoomResponseDto {
    private Long id;

    @NotBlank(message = "Tên lớp học không được để trống")
    private String name;

    @NotBlank(message = "Tiêu đề lớp học không được để trống")
    private String title;

    private Long teacherId;

    private String teacherName;

    private String code;

    private String status;

    private LocalDateTime createdDate;

    private Integer studentCount;

    private List<LessonDto> lessons;

    private List<ExamDto> exams;
}
