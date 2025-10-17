package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String description;

    private Long classroomId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean hasHomework = true;

    private String homeworkInstructions;

    private LocalDateTime homeworkDeadline;

    private Integer homeworkMaxScore;

    private String homeworkAttachmentUrl;
}
