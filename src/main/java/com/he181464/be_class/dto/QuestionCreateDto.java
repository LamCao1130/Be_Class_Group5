package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDto {
    private QuestionTypeDto questionTypeDto;

    private ReadingDto readingDto;

}
