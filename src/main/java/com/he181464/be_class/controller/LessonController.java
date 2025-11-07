package com.he181464.be_class.controller;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        lessonDto.setId(null);
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping("/get-detail/{id}")
    public ResponseEntity<?> getLessonDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonDetailById(id));
    }

    @GetMapping("/get-by-classRoomId/{id}")
    public ResponseEntity<?> getLessonsByClassRoomId(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonsByClassRoomId(id));
    }
}
