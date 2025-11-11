package com.he181464.be_class.service;

import com.he181464.be_class.dto.ExamAttemptsDTO;
import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.dto.UserAnswerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamService {
    List<ExamDto> getExamsByClassId(Long classId);

    ExamDto createExam(ExamDto examDto);

    ExamDto getExam(Integer id);

    List<ExamDto> getClassroomComingExam(Long id);

    Integer getPoint(UserAnswerDto userAnswerDto);

    List<ExamAttemptsDTO> getByClassroomId(Long classroomId);
}
