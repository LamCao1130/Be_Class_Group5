package com.he181464.be_class.service;

import com.he181464.be_class.dto.VocabularyDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VocabService {
    List<VocabularyDto> createListVocabulary(List<VocabularyDto> vocabularyCreateDtos);

    VocabularyDto updateVocabulary(VocabularyDto vocabularyDto);

    void deleteVocabulary(Long id);

    VocabularyDto editVocab(VocabularyDto vocabularyDto);

    List<VocabularyDto> getVocabulariesByLesson(Long id);

    List<VocabularyDto> importVocabFromExcelFile(MultipartFile file, Long lessonId);
}
