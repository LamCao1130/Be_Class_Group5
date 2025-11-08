package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.QuestionDto;
import com.he181464.be_class.dto.QuestionOptionDto;
import com.he181464.be_class.dto.QuestionTypeDto;
import com.he181464.be_class.entity.QuestionType;
import com.he181464.be_class.mapper.QuestionMapper;
import com.he181464.be_class.mapper.QuestionOptionMapper;
import com.he181464.be_class.mapper.QuestionTypeMapper;
import com.he181464.be_class.repository.QuestionTypeRepository;
import com.he181464.be_class.service.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionTypeServiceImpl implements QuestionTypeService {


    private final QuestionTypeRepository questionTypeRepository;

    private final QuestionTypeMapper questionTypeMapper;

    private final QuestionMapper questionMapper;

    private final QuestionOptionMapper questionOptionMapper;

    @Override
    public List<QuestionTypeDto> getQuestionTypesByLessonId(Long lessonId) {
        List<QuestionType> questionTypes = questionTypeRepository.findByLessonId(lessonId);
        return questionTypes.stream()
                .map(q -> {
                    QuestionTypeDto dto = questionTypeMapper.toQuestionTypeDto(q);
                    List<QuestionDto> questionDtos = q.getQuestions().stream()
                            .map(questionMapper::toQuestionDto)
                            .toList();
                    dto.setQuestions(questionDtos);
                    return dto;
                })
                .toList();
    }

    @Override
    public List<QuestionTypeDto> getQuestionTypesByLessonAndType(Long lessonId, String type) {
        List<QuestionType> questionTypes = questionTypeRepository.findByLessonIdAndType(lessonId, type);
        return questionTypes.stream()
                .map(q -> {
                    QuestionTypeDto dto = questionTypeMapper.toQuestionTypeDto(q);
                    List<QuestionDto> questionDtos = q.getQuestions().stream()
                            .map(q2 -> {
                                QuestionDto questionDto = questionMapper.toQuestionDto(q2);
                                List<QuestionOptionDto> optionDtos = q2.getQuestionOptions().stream()
                                        .map(questionOptionMapper::toQuestionOptionDto)
                                        .toList();
                                questionDto.setOptions(optionDtos);
                                return questionDto;
                            })
                            .toList();
                    dto.setQuestions(questionDtos);
                    return dto;
                })
                .toList();
    }
}
