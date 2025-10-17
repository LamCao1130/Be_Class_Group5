package com.he181464.be_class.service;

import com.he181464.be_class.dto.LessonDto;

import java.util.List;

public interface LessonService {

    LessonDto createLesson(LessonDto lessonDto);

    LessonDto updateLesson(LessonDto lessonDto);

    void deleteLesson(Long id);

    List<LessonDto> getAllLessonByClassroomId(Long id);

    void updateStatus(Long id);

}
