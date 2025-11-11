package com.he181464.be_class.service;

import com.he181464.be_class.dto.ExamDto;

import java.util.List;

public interface ExamService {
    List<ExamDto> getExamsByClassId(Long classId);

    ExamDto createExam(ExamDto examDto);

    ExamDto getExam(Integer id);

    List<ExamDto> getClassroomComingExam(Long id);
}
