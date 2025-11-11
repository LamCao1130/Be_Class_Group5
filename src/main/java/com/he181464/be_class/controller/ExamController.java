package com.he181464.be_class.controller;

import com.he181464.be_class.dto.ExamAttemptsDTO;
import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.dto.UserAnswerDto;
import com.he181464.be_class.repository.ExamAttemptsRepository;
import com.he181464.be_class.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/getPoint")
    public ResponseEntity<?>getPoint(@RequestBody UserAnswerDto userAnswerDto){
        return ResponseEntity.ok(examService.getPoint(userAnswerDto));
    }

    @GetMapping("/classroom/{id}/examHistory")
    public ResponseEntity<?> examHistory(@PathVariable("id") Long id
                                         ) {
        List<ExamAttemptsDTO> examAttemptsDTOS = examService.getByClassroomId(id);
        return ResponseEntity.ok(examAttemptsDTOS);
    }
}
