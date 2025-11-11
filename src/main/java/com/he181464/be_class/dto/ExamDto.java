package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDto {
    private Integer id;
    private String title;
    private String examType;
    private String description;
    private Integer durationMinutes;
    private Integer totalMarks;
    private Integer passingScore;
    private Integer switchTabs;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime examDate;
    private LocalDateTime examDateEnd;

    private Long createdBy;
    private String creatorName;
    private Long classRoomId;
    private String classRoomName;
}
