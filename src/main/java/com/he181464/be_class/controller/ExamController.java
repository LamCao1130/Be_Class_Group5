package com.he181464.be_class.controller;

import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/get-by-classRoomId/{id}")
    public ResponseEntity<?> getExamByClassRoomId(@PathVariable Long id){
        return ResponseEntity.ok(examService.getExamsByClassId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody ExamDto examDto) {
        return ResponseEntity.ok(examService.createExam(examDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExam(@PathVariable Integer id) {
        return ResponseEntity.ok(examService.getExam(id));
    }

    @GetMapping("/comingTest/{id}")
    public ResponseEntity<?> getComingExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getClassroomComingExam(id));
    }
}
