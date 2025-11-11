package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.HomeworkSubmissionDTO;
import com.he181464.be_class.entity.HomeworkSubmissions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfig.class)
public interface HomeworkSubmissionMapper {

    @Mapping(source = "id", target = "submissionId")
    @Mapping(source = "lessonId.title", target = "lessonName")
    @Mapping(source = "studentId.id", target = "studentId")
    @Mapping(source = "lessonId.homeworkDeadline", target = "deadline")
    HomeworkSubmissionDTO homeworkSubmissionToDto(HomeworkSubmissions entity);
}
