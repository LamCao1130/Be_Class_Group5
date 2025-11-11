package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeWorkStatusDto {

    private String studentName;

    private String status;

    private Integer numberWrong;

    private LocalDateTime submittedAt;

    private String type;

    private String answerText;
}
