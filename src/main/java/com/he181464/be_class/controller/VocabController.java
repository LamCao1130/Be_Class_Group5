package com.he181464.be_class.controller;

import com.he181464.be_class.dto.VocabularyCreateDto;
import com.he181464.be_class.model.response.VocabularyResponse;
import com.he181464.be_class.service.VocabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/public/vocab")
@RequiredArgsConstructor

public class VocabController {
    private final VocabService vocabService;

    @PostMapping("create")
    public ResponseEntity<VocabularyResponse> createVocab(@Valid @RequestBody VocabularyCreateDto vocabularyCreateDto){
        return ResponseEntity.ok(vocabService.createVocabulary(vocabularyCreateDto));
    }

    @PostMapping("createMutipleVocab")
    public ResponseEntity<?> createMutipleVocab(@Validated @RequestBody List<VocabularyCreateDto> vocabularyCreateDto){
        vocabService.createListVocabulary(vocabularyCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/lesson/{id}")
    public ResponseEntity<List<VocabularyResponse>> getVocabById(@PathVariable("id") Long lessonId) {
        List<VocabularyResponse> vocabularies = vocabService.getVocabulariesByLesson(lessonId);
        return ResponseEntity.ok(vocabularies);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVocab(@PathVariable("id") Long vocabId){
        vocabService.deleteVocabulary(vocabId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("deleteMultiple")
    public ResponseEntity<?> deleteMultiple(@RequestBody List<Long> vocabIds){
        vocabService.deleteMultipleVocabularies(vocabIds);
        return ResponseEntity.ok().build();
    }
}
