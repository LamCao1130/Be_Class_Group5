package com.he181464.be_class.service;

import com.he181464.be_class.dto.QuestionTypeDto;

import java.util.List;

public interface QuestionTypeService {

    List<QuestionTypeDto> getQuestionTypesByLessonId(Long lessonId);

    List<QuestionTypeDto> getQuestionTypesByLessonAndType(Long lessonId, String type);

}
