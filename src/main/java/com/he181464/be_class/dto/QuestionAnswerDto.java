package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerDto {

    private Integer id;
    private String userAnswer;
    private String result;
    private Long SubmissionHistoryId;
}
