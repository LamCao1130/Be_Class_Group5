package com.he181464.be_class.controller;

import com.he181464.be_class.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/get-by-classRoomId/{id}")
    public ResponseEntity<?> getExamByClassRoomId(@PathVariable Long id){
        return ResponseEntity.ok(examService.getExamsByClassId(id));
    }
}
