package com.he181464.be_class.controller;

import com.he181464.be_class.dto.VocabularyDto;
import com.he181464.be_class.service.VocabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/v1/public/vocab")
@RequiredArgsConstructor

public class VocabController {
    private final VocabService vocabService;

    @PostMapping("create")
    public ResponseEntity<?> createVocab(@Valid @RequestBody List<VocabularyDto> vocabularyCreateDto){
        vocabularyCreateDto.stream().forEach(voc->voc.setId(null));
        return ResponseEntity.ok(vocabService.createListVocabulary(vocabularyCreateDto));
    }

    @GetMapping("/lesson/{id}")
    public ResponseEntity<?> getVocabById(@PathVariable("id") Long lessonId) {
        List<VocabularyDto> vocabularies = vocabService.getVocabulariesByLesson(lessonId);
        return ResponseEntity.ok(vocabularies);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVocab(@PathVariable("id") Long vocabId){
        vocabService.deleteVocabulary(vocabId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("import")
    public ResponseEntity<?> importVocab(@RequestParam("file") MultipartFile file,
                                         @RequestParam("lessonId") Long lessonId) {
        vocabService.importVocabFromExcelFile(file, lessonId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
