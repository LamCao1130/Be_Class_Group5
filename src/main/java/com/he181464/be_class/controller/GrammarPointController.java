package com.he181464.be_class.controller;

import com.he181464.be_class.service.GrammarPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/grammar")
@RequiredArgsConstructor
public class GrammarPointController {
    private final GrammarPointService grammarPointService;

    @GetMapping("/get-by-lesson/{id}")
    public ResponseEntity<?> getByLessonId(@PathVariable int id) {
        return ResponseEntity.ok(grammarPointService.getByLessonId(id));
    }
}
