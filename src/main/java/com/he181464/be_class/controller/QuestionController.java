package com.he181464.be_class.controller;

import com.he181464.be_class.dto.AnswerCheckDto;
import com.he181464.be_class.dto.QuestionCreateDto;
import com.he181464.be_class.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/Question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?>getByLessonId(@PathVariable Long lessonId){
        return ResponseEntity.ok(questionService.getListQuestionByLesson(lessonId));
    }


    @PostMapping("/create")
    public ResponseEntity<?>createListQuestion(@RequestBody QuestionCreateDto questionCreateDto){
        return ResponseEntity.ok(questionService.createListQuestion(questionCreateDto));
    }

    @DeleteMapping("/deleteByQuestionType/{id}")
    public ResponseEntity<?>deleteQuestionByQuestionType(@PathVariable Integer id){
        questionService.deleteQuestionByQuestionType(id);
        return ResponseEntity.ok("Deleted");
    }
    
    @DeleteMapping("delete/reading/{id}")
    public ResponseEntity<?>deleteReading(@PathVariable Integer id){
        questionService.deleteReadingPassage(id);
        return ResponseEntity.ok("Deleted Reading passage success");
    }

    @DeleteMapping("delete/listening/{id}")
    public ResponseEntity<?>deleteListening(@PathVariable Integer id){
        questionService.deleteListeningPassage(id);
        return ResponseEntity.ok("Deleted Listening passage success");
    }

    @PutMapping("/update")
    public ResponseEntity<?>updateQuetion(@RequestBody QuestionCreateDto questionCreateDto){
        return ResponseEntity.ok(questionService.updateQuestion(questionCreateDto));
    }

    @GetMapping("/questionType/{id}")
    public ResponseEntity<?>getByQuestionType(@PathVariable Integer id){
        return ResponseEntity.ok(questionService.getQuestionByQuestionTypeId(id));
    }

    @PostMapping("/check-answer" )
    public ResponseEntity<?>checkAnswer(@RequestBody List<AnswerCheckDto> answers){
        return ResponseEntity.ok(questionService.checkAnswer(answers));
    }
}
