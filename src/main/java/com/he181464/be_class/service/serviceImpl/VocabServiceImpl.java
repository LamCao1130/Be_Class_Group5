package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.VocabularyCreateDto;
import com.he181464.be_class.dto.VocabularyUpdateDto;
import com.he181464.be_class.entity.Lesson;
import com.he181464.be_class.entity.Vocabulary;
import com.he181464.be_class.mapper.VocabMapper;
import com.he181464.be_class.model.response.VocabularyResponse;
import com.he181464.be_class.repository.LessonRepository;
import com.he181464.be_class.repository.VocabRepository;
import com.he181464.be_class.service.VocabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VocabServiceImpl implements VocabService {

    private final VocabRepository vocabRepository;
    private final VocabMapper vocabMapper;
    private final LessonRepository lessonRepository;

    @Override
    public void createListVocabulary(List<VocabularyCreateDto> vocabularyCreateDtos) {
        List<Vocabulary> vocabularies = vocabularyCreateDtos.stream()
                .map(vocabMapper::toVocabulary)
                .toList();
        vocabRepository.saveAll(vocabularies);
    }

    @Override
    public VocabularyResponse createVocabulary(VocabularyCreateDto vocabularyCreateDto) {
        Lesson lesson = lessonRepository.findById(vocabularyCreateDto.getLessonId())
                .orElseThrow(()-> new IllegalArgumentException(("Khong tim thay lesson Id")));
        Vocabulary newVocabulary = vocabMapper.toVocabulary(vocabularyCreateDto);
        newVocabulary.setLesson(lesson);
        return vocabMapper.toVocabularyResponse(vocabRepository.save(newVocabulary));

    }

    @Override
    public VocabularyResponse updateVocabulary(VocabularyUpdateDto vocabularyDto) {
        if(vocabularyDto.getId() == null) {
            throw new IllegalArgumentException("ID vocab không được để trống");
        }
        Vocabulary vocabulary = vocabRepository.findById(vocabularyDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay id vocab"));
         vocabRepository.save(vocabMapper.updateVocabulary(vocabulary,vocabularyDto));
        return vocabMapper.toVocabularyResponse(vocabMapper.updateVocabulary(vocabulary,vocabularyDto));
    }

    @Override
    public void deleteVocabulary(Long id) {
        vocabRepository.deleteById(id);
    }

    @Override
    public void deleteMultipleVocabularies(List<Long> id) {
        vocabRepository.deleteAllById(id);
    }

    @Override
    public List<VocabularyResponse> getVocabulariesByLesson(Long id) {
        return vocabRepository.findByLessonId(id).stream()
                .map((item) ->vocabMapper.toVocabularyResponse(item.orElseThrow())).toList();
    }


}
