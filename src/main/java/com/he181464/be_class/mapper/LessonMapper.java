package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface LessonMapper {
    Lesson toLessonEntity(LessonDto lesson);

    LessonDto toLessonDto(Lesson lesson);
}
