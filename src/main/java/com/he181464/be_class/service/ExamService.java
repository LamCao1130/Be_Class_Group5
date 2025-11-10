package com.he181464.be_class.service;

import com.he181464.be_class.dto.ExamDto;

import java.util.List;

public interface ExamService {
    List<ExamDto> getExamsByClassId(Long classId);
}
