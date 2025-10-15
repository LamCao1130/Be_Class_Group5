package com.he181464.be_class.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDto {
    private Long id;

    @NotBlank(message = "Tên lớp học không được để trống")
    private String name;

    @NotBlank(message = "Tiêu đề lớp học không được để trống")
    private String title;

    private Long teacherId;

    private String teacherName;

    private LocalDateTime createdDate;
}
