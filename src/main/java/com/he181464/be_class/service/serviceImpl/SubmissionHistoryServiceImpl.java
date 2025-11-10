package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.SubmissionHistoryDto;
import com.he181464.be_class.entity.SubmissionHistory;
import com.he181464.be_class.mapper.SubmissionHistoryMapper;
import com.he181464.be_class.repository.SubmissionHistoryRepository;
import com.he181464.be_class.service.SubmissionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionHistoryServiceImpl implements SubmissionHistoryService {

    private final SubmissionHistoryRepository submissionHistoryRepository;

    private final SubmissionHistoryMapper submissionHistoryMapper;

    @Override
    public SubmissionHistoryDto createSubmissionHistory(SubmissionHistoryDto submissionHistoryDto) {
        SubmissionHistory submissionHistory = submissionHistoryMapper.toSubmissionHistoryEntity(submissionHistoryDto);
        submissionHistory.setId(null); // Ensure the ID is null for creation
        submissionHistory.setType(submissionHistoryDto.getType());
        submissionHistory.setNumberWrong(submissionHistoryDto.getNumberWrong());
        submissionHistory.setSubmittedAt(LocalDateTime.now());
        submissionHistory.setStudentId(submissionHistoryDto.getStudentId());
        submissionHistory.setLessonId(submissionHistoryDto.getLessonId());
        submissionHistory.setAnswerWriting(submissionHistoryDto.getAnswerWriting());
        submissionHistoryRepository.save(submissionHistory);
        return submissionHistoryMapper.toSubmissionHistoryDto(submissionHistory);
    }
}