package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.*;
import com.he181464.be_class.mapper.QuestionMapper;
import com.he181464.be_class.mapper.QuestionOptionMapper;
import com.he181464.be_class.mapper.QuestionTypeMapper;
import com.he181464.be_class.mapper.ReadingMapper;
import com.he181464.be_class.repository.*;
import com.he181464.be_class.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionTypeRepository questionTypeRepository;
    private final LessonRepository lessonRepository;
    private final QuestionTypeMapper questionTypeMapper;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionOptionMapper questionOptionMapper;
    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;

    @Transactional
    @Override
    public QuestionCreateDto createListQuestion(QuestionCreateDto questionCreateDto) {

        Lesson lesson = lessonRepository.findById(questionCreateDto.getQuestionTypeDto().getLessonId()).orElseThrow(() -> new IllegalArgumentException("Khong tim thay lesson voi id la"));
        ReadingDto readingDto = questionCreateDto.getReadingDto();
        ReadingPassage savedReading = null;
        if (readingDto != null) {
            ReadingPassage readingEntity = readingMapper.toReadingPassageEntity(readingDto);
            readingEntity.setLesson(lesson);
            savedReading = readingRepository.save(readingEntity);
        }
        QuestionType questionType = questionTypeMapper.toQuestionTypeEntity(questionCreateDto.getQuestionTypeDto());
        questionType.setLesson(lesson);
        questionType.setType(questionCreateDto.getQuestionTypeDto().getType());
        List<QuestionDto> questionDtos = questionCreateDto.getQuestionTypeDto().getQuestions();
        List<Questions> questions = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {

            Questions question = questionMapper.toQuestionEntity(questionDto);
            question.setQuestionType(questionType);
            question.setCreatedAt(LocalDateTime.now());
            if (savedReading != null) {
                question.setReadingPassage(savedReading);
            }

            if (questionDto.getOptions() != null) {
                List<QuestionOption> questionOptions = questionDto.getOptions().stream().map(
                        questionOptionMapper::toQuestionOptionEntity).toList();
                question.setQuestionOptions(questionOptions);
                for (QuestionOption option : question.getQuestionOptions()) {
                    option.setQuestions(question);
                }
            }
            questions.add(question);
        }
        questionType.setQuestions(questions);
        QuestionType savedType = questionTypeRepository.save(questionType);
        questionCreateDto.getQuestionTypeDto().setQuestionTypeId(savedType.getId());
        return questionCreateDto;
    }



}
