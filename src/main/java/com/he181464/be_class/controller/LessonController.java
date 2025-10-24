package com.he181464.be_class.controller;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        lessonDto.setId(null);
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(createdLesson);
    }


}
