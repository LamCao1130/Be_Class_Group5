package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = MapstructConfig.class)
public interface LessonMapper {
    LessonDto toLessonDto(Lesson lesson);

    Lesson toLessonEntity(LessonDto lessonDto);
    @Mapping(target = "createdAt", ignore = true)
    Lesson updateLesson(LessonDto lessonDto, @MappingTarget Lesson lesson);

}
