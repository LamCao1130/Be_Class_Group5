package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.GrammarPointDto;
import com.he181464.be_class.mapper.GrammarPointMapper;
import com.he181464.be_class.repository.GrammarPointRepository;
import com.he181464.be_class.service.GrammarPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GrammarPointServiceImpl implements GrammarPointService {

    private final GrammarPointRepository grammarPointRepository;
    private final GrammarPointMapper grammarPointMapper;

    @Override
    public List<GrammarPointDto> getByLessonId(int lessonId) {
        List<GrammarPointDto> grammarPointDtos = grammarPointRepository.findByLessonId(lessonId)
                .stream().map(grammarPointMapper::toGrammarPointDto).
                toList();
        return grammarPointDtos;
    }
}
