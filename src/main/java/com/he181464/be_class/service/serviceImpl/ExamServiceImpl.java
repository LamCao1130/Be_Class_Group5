package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.ExamAttemptsDTO;
import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.dto.UserAnswerDto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.entity.Exam;
import com.he181464.be_class.entity.ExamAttempts;
import com.he181464.be_class.entity.QuestionOption;
import com.he181464.be_class.mapper.ExamAttemptMapper;
import com.he181464.be_class.mapper.ExamMapper;
import com.he181464.be_class.repository.*;
import com.he181464.be_class.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final ClassRoomRepository classRoomRepository;
    private final AccountRepository accountRepository;
    private final QuestionRepository questionRepository;
    private final ExamAttemptsRepository examAttemptsRepository;
    private final ExamAttemptMapper examAttemptMapper;
    @Override
    public List<ExamDto> getExamsByClassId(Long classId) {
        List<Exam> exams= examRepository.findByClassRoomId(classId);
        List<Exam> attemptedExams = examAttemptsRepository.findExamAttemptsByClassroomId(classId)
                .stream()
                .map(item -> item.getExam())
                .toList();

        List<Exam> availableExams = exams.stream()
                .filter(exam -> {
                    boolean isNotEnded = exam.getExamDateEnd().isAfter(LocalDateTime.now());
                    boolean isNotAttempted = !attemptedExams.contains(exam);
                    return isNotEnded && isNotAttempted;
                })
                .toList();

        return availableExams.stream()
                .map(examMapper::toDTO)
                .toList();
    }

    @Override
    public ExamDto createExam(ExamDto examDto) {
        Exam exam = examMapper.toEntity(examDto);
        exam.setClassRoom(classRoomRepository.findById(examDto.getClassRoomId()).orElseThrow(
                () -> new IllegalArgumentException("Khong tim thay classroomId")
        ));
        exam.setCreatedAt(LocalDateTime.now());
        exam.setExamDateEnd(examDto.getExamDate().plusMinutes(examDto.getDurationMinutes()));
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

    @Override
    public Integer getPoint(UserAnswerDto userAnswerDto) {
        Integer count = 0;
        List<QuestionOption> questionOptions = questionRepository.findQuestionOptionCorrectByExam(userAnswerDto.getExamId());
        Map<Integer, List<QuestionOption>> questionOptionByQuestion = questionOptions.
                stream().collect(Collectors.groupingBy(questionOption -> questionOption.getQuestions().getId()));
        for (Map.Entry<Integer, List<QuestionOption>> entry : questionOptionByQuestion.entrySet()) {
            Integer questionId = entry.getKey();
            List<Integer> questionOptions1 = entry.getValue().stream().map(QuestionOption::getId).toList();
            List<Integer> userOptions = userAnswerDto.getAnswers().get(questionId);
            if (userOptions != null) {
                List<Integer> correctOptions = new ArrayList<>(questionOptions1);

                Collections.sort(userOptions);
                Collections.sort(correctOptions);

                if (userOptions.equals(correctOptions)) {
                    count++;
                }
            }
        }


        ExamAttempts examAttempts = new ExamAttempts();
        examAttempts.setExam(examRepository.findById(userAnswerDto.getExamId()).orElseThrow());
        examAttempts.setStartedAt(userAnswerDto.getStartedAt());
        examAttempts.setSubmittedAt(userAnswerDto.getSubmittedAt());
        examAttempts.setTimeSpent(userAnswerDto.getTimeSpent());
        examAttempts.setStudent(accountRepository.findAccountsById(userAnswerDto.getStudentId()));
        int totalQuestions = questionOptionByQuestion.size();
        double scoreRatio = (double) count / totalQuestions;
        Integer mark = 0;
        double rawScore = scoreRatio * examAttempts.getExam().getTotalMarks();
        examAttempts.setStatus("1");
        mark = (int) Math.round(rawScore);
        examAttempts.setScore(mark);
        examAttemptsRepository.save(examAttempts);
        return mark;
    }

    @Override
    public List<ExamAttemptsDTO> getByClassroomId(Long classroomId) {
        List<ExamAttempts> listExam = examAttemptsRepository.findExamAttemptsByClassroomId(classroomId);

        return listExam.stream().map(examAttempts -> {
            ExamAttemptsDTO examAttemptsDTO = examAttemptMapper.toExamAttemptsDTO(examAttempts);
            examAttemptsDTO.setEmail( examAttempts.getStudent().getEmail());
            return examAttemptsDTO;
        }).toList();
    }
}
