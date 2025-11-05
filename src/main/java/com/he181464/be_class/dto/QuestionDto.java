package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Integer id;

    private String questionText;

    private String correctAnswer;
    private String explanation;

    private Integer marks;

    private String difficulty;

    private Integer readingPassageId;

    private Integer questionTypeId;


    private List<QuestionOptionDto> options;
}
