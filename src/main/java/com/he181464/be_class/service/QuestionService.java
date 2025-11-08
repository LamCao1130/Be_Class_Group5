package com.he181464.be_class.service;

import com.he181464.be_class.dto.AnswerCheckDto;
import com.he181464.be_class.dto.AnswerCheckedDto;
import com.he181464.be_class.dto.QuestionCreateDto;

import java.util.List;

public interface QuestionService {
    QuestionCreateDto createListQuestion(QuestionCreateDto questionCreateDto);

    List<QuestionCreateDto> getListQuestionByLesson(Long id);

    void deleteQuestionByQuestionType(Integer id);

    void deleteReadingPassage(Integer id);

    void deleteListeningPassage(Integer id);

    QuestionCreateDto updateQuestion(QuestionCreateDto questionCreateDto);

    QuestionCreateDto getQuestionByQuestionTypeId(Integer id);

    List<AnswerCheckedDto> checkAnswer(List<AnswerCheckDto> answers);
}
