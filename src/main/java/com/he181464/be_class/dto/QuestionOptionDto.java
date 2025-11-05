package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionDto {
    private Integer id;

    private String optionText   ;

    private Integer questionId;

    private Boolean correctAnswer;
}
