package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.entity.Lesson;
import com.he181464.be_class.mapper.LessonMapper;
import com.he181464.be_class.repository.ClassRoomRepository;
import com.he181464.be_class.repository.LessonRepository;
import com.he181464.be_class.repository.VocabRepository;
import com.he181464.be_class.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final VocabRepository vocabRepository;
    private final ClassRoomRepository classRoomRepository;

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toLessonEntity(lessonDto);
        lesson.setClassroom(classRoomRepository.findById(lessonDto.getClassroomId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy classroom với id")));
        lesson.setCreatedAt(LocalDateTime.now());
        lessonRepository.save(lesson);
        return lessonDto;
    }

    @Override
    public LessonDto updateLesson(LessonDto lessonDto) {
        if (lessonDto.getId() == null) {
            throw new IllegalArgumentException("Không thể để trống id lesson");
        }
        Lesson lesson = lessonRepository.findById(lessonDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson không tồn tại"));
        lesson = lessonMapper.updateLesson(lessonDto,lesson);
        lesson.setUpdatedAt(LocalDateTime.now());
        lessonRepository.save(lesson);

        return lessonDto;
    }

    @Override
    public void deleteLesson(Long id) {
        if(!lessonRepository.existsById(id)){
            throw new IllegalArgumentException("Không tìm thấy Id");
        }
        vocabRepository.deleteByLessonId(id);
        lessonRepository.deleteById(id);
    }

    @Override
    public List<LessonDto> getAllLessonByClassroomId(Long id) {
        if(!lessonRepository.existsByClassroomId(id)){
            throw  new IllegalArgumentException("Không tìm thấy lesson nào của id " +id);
        }
        List<Lesson> lessons = lessonRepository.findAllByClassroomId(id);
        List<LessonDto>lessonDtos = lessons.stream().map( item ->{
            LessonDto dto =  lessonMapper.toLessonDto(item);
            dto.setClassroomId(item.getClassroom().getId());
            return dto;
        }).toList();
        return lessonDtos;
    }

    @Override
    public void updateStatus(Long id) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khônt tìm thấy lesson id"));
        lesson.setPublish(!lesson.isPublish());
        lessonRepository.save(lesson);
    }
}
