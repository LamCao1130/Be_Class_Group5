package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.QuestionOptionDto;
import com.he181464.be_class.entity.QuestionOption;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface QuestionOptionMapper {
    QuestionOption toQuestionOptionEntity(QuestionOptionDto questionOptionDto);

    QuestionOptionDto toQuestionOptionDto(QuestionOption questions);
}
