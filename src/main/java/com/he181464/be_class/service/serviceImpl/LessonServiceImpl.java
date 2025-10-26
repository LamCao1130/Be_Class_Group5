package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.entity.Lesson;
import com.he181464.be_class.mapper.LessonMapper;
import com.he181464.be_class.repository.LessonRepository;
import com.he181464.be_class.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonMapper lessonMapper;

    private final LessonRepository lessonRepository;

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toLessonEntity(lessonDto);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonDto(lesson);
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        return null;
    }

    @Override
    public List<LessonDto> getLessonsByClassRoomId(Long classRoomId) {
        List<Lesson> lessons = lessonRepository.findByClassRoomId(classRoomId);
        return lessons.stream().map(lessonMapper::toLessonDto).toList();
    }
}
