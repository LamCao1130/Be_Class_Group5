package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionHistoryDto {
    private Long id;
    private String type;
    private Integer numberWrong;
    private LocalDateTime submittedAt;
    private Long studentId;
    private Long lessonId;
    private String answerWriting;

}
