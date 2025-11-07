package com.he181464.be_class.controller;

import com.he181464.be_class.dto.QuestionTypeByTypeDto;
import com.he181464.be_class.service.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/question-type")
public class QuestionTypeController {

    private final QuestionTypeService questionTypeService;

    @GetMapping("/lesson/{id}")
    public ResponseEntity<?> getQuestionTypesByLessonId(@PathVariable("id") Long lessonId) {
        return ResponseEntity.ok(questionTypeService.getQuestionTypesByLessonId(lessonId));
    }

    @PostMapping("/get-by-type")
    public ResponseEntity<?> getQuestionTypesByType(@RequestBody QuestionTypeByTypeDto dto) {
        return ResponseEntity.ok(questionTypeService.getQuestionTypesByLessonAndType(dto.getLessonId(), dto.getType()));
    }

}
