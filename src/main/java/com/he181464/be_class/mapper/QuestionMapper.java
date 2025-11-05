package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.QuestionDto;
import com.he181464.be_class.entity.Questions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfig.class)

public interface QuestionMapper {

    Questions toQuestionEntity(QuestionDto questionDto);
    QuestionDto toQuestionDto(Questions questions);
}
