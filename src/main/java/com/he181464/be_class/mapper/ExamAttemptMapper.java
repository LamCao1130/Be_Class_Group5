package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ExamAttemptsDTO;
import com.he181464.be_class.entity.ExamAttempts;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper( config = MapstructConfig.class)
public interface ExamAttemptMapper {

    @Mapping(target = "examId", source = "exam.id")
    @Mapping(target = "examTitle", source = "exam.title")
    ExamAttemptsDTO toExamAttemptsDTO(ExamAttempts examAttempts);
}
