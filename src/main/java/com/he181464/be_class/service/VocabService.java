package com.he181464.be_class.service;

import com.he181464.be_class.dto.VocabularyCreateDto;
import com.he181464.be_class.dto.VocabularyUpdateDto;
import com.he181464.be_class.model.response.VocabularyResponse;

import java.util.List;

public interface VocabService {
    void createListVocabulary(List<VocabularyCreateDto> vocabularyCreateDtos);

    VocabularyResponse createVocabulary(VocabularyCreateDto vocabularyCreateDto);

    VocabularyResponse updateVocabulary(VocabularyUpdateDto vocabularyDto);

    void deleteVocabulary(Long id);

    void deleteMultipleVocabularies(List<Long> id);

    List<VocabularyResponse> getVocabulariesByLesson(Long id);
}
