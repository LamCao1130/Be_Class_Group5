package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCheckedDto {

    private Integer questionId;

    private String questionText;

    private List<QuestionOptionDto> selectTrueOptions;

    private String textTrueAnswer;

}
