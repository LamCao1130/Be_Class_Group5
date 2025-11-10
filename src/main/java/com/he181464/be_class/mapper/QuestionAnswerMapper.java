package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.QuestionAnswerDto;
import com.he181464.be_class.entity.QuestionAnswers;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface QuestionAnswerMapper {
    QuestionAnswers toQuestionAnswerEntity(QuestionAnswerDto questionAnswerDto);

    QuestionAnswerDto toQuestionAnswerDto(QuestionAnswers questionAnswers);
}
