package com.he181464.be_class.controller;

import com.he181464.be_class.dto.QuestionCreateDto;
import com.he181464.be_class.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/Question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

//    @GetMapping("/lesson/{id}")
//    public ResponseEntity<?>getByLessonId(@PathVariable Long lessonId){
//        return ResponseEntity.ok(questionTypeService.findByLessonId(lessonId));
//    }


    @PostMapping("/create")
    public ResponseEntity<?>createListQuestion(@RequestBody QuestionCreateDto questionCreateDto){
        return ResponseEntity.ok(questionService.createListQuestion(questionCreateDto));
    }
}
