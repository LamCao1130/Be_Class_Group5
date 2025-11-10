package com.he181464.be_class.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkSubmissionDTO {
    private int submissionId;
    private String lessonName;
    private LocalDateTime deadline;
    private LocalDateTime submittedAt;
    private int studentId;
    private BigDecimal score;
    private String status;
    private String teacherFeedback;
    private String gradedBy;
    private int attemptCount;
    private int timeSpent;
}
