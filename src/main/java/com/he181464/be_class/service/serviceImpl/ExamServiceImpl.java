package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.entity.Exam;
import com.he181464.be_class.mapper.ExamMapper;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.repository.ClassRoomRepository;
import com.he181464.be_class.repository.ExamRepository;
import com.he181464.be_class.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final ClassRoomRepository classRoomRepository;
    private final AccountRepository accountRepository;
    @Override
    public List<ExamDto> getExamsByClassId(Long classId) {
        List<Exam> exams= examRepository.findByClassRoomId(classId);
        return exams.stream().map(examMapper::toDTO).toList();
    }

    @Override
    public ExamDto createExam(ExamDto examDto) {
        Exam exam = examMapper.toEntity(examDto);
        exam.setClassRoom(classRoomRepository.findById(examDto.getClassRoomId()).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay classroomId")
        ));
        exam.setCreatedAt(new Date());
        exam.setAccount(accountRepository.findById(examDto.getCreatedBy()).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay account")));
        examRepository.save(exam);
        return examDto;
    }

    @Override
    public ExamDto getExam(Integer id) {
        return examMapper.toDTO(examRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay exam id")
        ));

    }

    @Override
    public List<ExamDto> getClassroomComingExam(Long id) {
        LocalDateTime twoDayLefter = LocalDateTime.now().plusDays(2);
        return examRepository.getComingExam(id,twoDayLefter).stream().map(
                examMapper::toDTO
        ).toList();
    }
}
