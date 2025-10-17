package com.he181464.be_class.controller;

import com.he181464.be_class.dto.LessonDto;
import com.he181464.be_class.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/lesson")

public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/classroom/{id}")
    public ResponseEntity<?> getLessonByClassroom(@PathVariable Long id){
        return ResponseEntity.ok(lessonService.getAllLessonByClassroomId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@Valid @RequestBody LessonDto lessonDto){
        return ResponseEntity.ok(lessonService.createLesson(lessonDto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatusLesson(@PathVariable Long id){
        lessonService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLesson(@Valid @RequestBody LessonDto lessonDto){
        return ResponseEntity.ok(lessonService.updateLesson(lessonDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id){
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }
}
