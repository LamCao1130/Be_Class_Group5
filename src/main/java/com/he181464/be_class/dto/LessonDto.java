package com.he181464.be_class.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;

    @NotNull(message = "classRoomId cannot be null")
    private Long classRoomId;

    @NotNull(message = "title cannot be null")
    private String title;

    private String description;

    private Integer status;

    private String content;

    private Integer hasHomework;

    private String homeworkInstructions;

    private LocalDateTime homeworkDeadline;

    private Integer homeworkMaxScore;

    private String homeworkAttachmentUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
