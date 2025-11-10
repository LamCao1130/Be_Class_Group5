package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.entity.Exam;
import com.he181464.be_class.mapper.ExamMapper;
import com.he181464.be_class.repository.ExamRepository;
import com.he181464.be_class.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamMapper examMapper;
    @Override
    public List<ExamDto> getExamsByClassId(Long classId) {
        List<Exam> exams= examRepository.findByClassRoomId(classId);
        return exams.stream().map(examMapper::toDTO).toList();
    }
}
