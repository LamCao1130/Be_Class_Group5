package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingDto {
    private String title;

    private String passageContent;

    private Integer id;

    private Integer lessonId;

    private List<QuestionDto> questions;
}
