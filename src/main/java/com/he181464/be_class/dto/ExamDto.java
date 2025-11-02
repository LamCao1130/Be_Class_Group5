package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDto {
    private Long id;
    private Long classRoomId;
    private String title;
    private String examType;
    private String description;
    private Integer durationMinutes;
    private Integer totalMarks;
    private Integer passingScore;
    private LocalDateTime examDate;
    private Integer switchTabs;
    private Integer status;
    private LocalDateTime createdAt;

    private Integer createdBy;
    private String creatorName;

}