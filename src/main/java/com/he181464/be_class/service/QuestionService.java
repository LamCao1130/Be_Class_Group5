package com.he181464.be_class.service;

import com.beust.ah.A;
import com.he181464.be_class.dto.*;

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

    List<AnswerCheckedDto> checkAnswerVocab(List<AnswerCheckDto> answers, Long lessonId);

    List<AnswerCheckedDto> checkAnswerListen(List<AnswerCheckDto> answers, Long lessonId);

    List<AnswerCheckedDto> checkAnswerReading(List<AnswerCheckDto> answers, Long lessonId);

    List<ListeningPassageDto> getListeningPassageByLessonId(Long lessonId);

    List<ReadingDto> getReadingPassageByLessonId(Long lessonId);


}
