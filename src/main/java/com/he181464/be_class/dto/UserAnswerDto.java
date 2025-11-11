package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerDto {
    private Map<Integer, List<Integer>>answers;
    private Integer examId;
    private Integer timeSpent;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Long studentId;

}
