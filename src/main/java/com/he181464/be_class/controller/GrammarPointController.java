package com.he181464.be_class.controller;

import com.he181464.be_class.dto.GrammarPointDto;
import com.he181464.be_class.service.GrammarPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/grammar")
@RequiredArgsConstructor
public class GrammarPointController {
    private final GrammarPointService grammarPointService;

    @GetMapping("/get-by-lesson/{id}")
    public ResponseEntity<?> getByLessonId(@PathVariable int id) {
        return ResponseEntity.ok(grammarPointService.getByLessonId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?>create(@RequestBody  GrammarPointDto grammarPointDto){
        return ResponseEntity.ok(grammarPointService.create(grammarPointDto));
    }

    @PutMapping("/update")
    public ResponseEntity<?>update(@RequestBody GrammarPointDto grammarPointDto){
        return ResponseEntity.ok(grammarPointService.updateGrammar(grammarPointDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        grammarPointService.deleteGrammar(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?>getById(@PathVariable Integer id){
        return ResponseEntity.ok(grammarPointService.getById(id));
    }
}
