package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamAttemptsDTO {
    private int id;
    private int examId;
    private String examTitle;
    private BigDecimal score;
    private Integer timeSpent;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String status;
}
