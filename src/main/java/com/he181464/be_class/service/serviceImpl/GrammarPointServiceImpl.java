package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.GrammarPointDto;
import com.he181464.be_class.entity.GrammarPoint;
import com.he181464.be_class.mapper.GrammarPointMapper;
import com.he181464.be_class.repository.GrammarPointRepository;
import com.he181464.be_class.repository.LessonRepository;
import com.he181464.be_class.service.GrammarPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GrammarPointServiceImpl implements GrammarPointService {

    private final GrammarPointRepository grammarPointRepository;
    private final GrammarPointMapper grammarPointMapper;
    private final LessonRepository lessonRepository;

    @Override
    public List<GrammarPointDto> getByLessonId(int lessonId) {
        List<GrammarPointDto> grammarPointDtos = grammarPointRepository.findByLessonId(lessonId)
                .stream().map(grammarPointMapper::toGrammarPointDto).
                toList();
        return grammarPointDtos;
    }

    @Override
    public GrammarPointDto create(GrammarPointDto grammarPointDto) {
        GrammarPoint grammarPoint = grammarPointMapper.toGrammarPointEntity(grammarPointDto);
        grammarPoint.setLesson(lessonRepository.findById(grammarPointDto.getLessonId()).orElseThrow(
                () ->new IllegalArgumentException("Khong tim thay lesson id")
        ));
        grammarPointRepository.save(grammarPoint);
        return grammarPointDto;
    }

    @Override
    public GrammarPointDto updateGrammar(GrammarPointDto grammarPointDto) {
        GrammarPoint grammarPoint = grammarPointMapper.toGrammarPointEntity(grammarPointDto);
        grammarPoint.setLesson(lessonRepository.findById(grammarPointDto.getLessonId()).orElseThrow(
                () ->new IllegalArgumentException("Khong tim thay lesson id")
        ));
        grammarPointRepository.save(grammarPoint);
        return grammarPointDto;    }

    @Override
    public void deleteGrammar(Integer id) {
        grammarPointRepository.deleteById(id);
    }

    @Override
    public GrammarPointDto getById(Integer id) {
        GrammarPoint grammarPoint = grammarPointRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Khong tim thay grammar"));
        GrammarPointDto grammarPointDto = grammarPointMapper.toGrammarPointDto(grammarPoint);
        grammarPointDto.setLessonId(grammarPoint.getLesson().getId());
        return grammarPointDto;
    }
}
