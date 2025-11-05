package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTypeDto {
    private String name;

    private Long lessonId;

    private Integer examId;

    private Integer questionTypeId;

    private String type;

    private List<QuestionDto> questions;
}
