package com.he181464.be_class.mapper;


import com.he181464.be_class.dto.QuestionTypeDto;
import com.he181464.be_class.entity.QuestionType;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface QuestionTypeMapper {
    QuestionTypeDto toQuestionTypeDto(QuestionType questionType);

    QuestionType toQuestionTypeEntity(QuestionTypeDto questionTypeDto);

}
