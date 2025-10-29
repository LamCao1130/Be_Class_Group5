package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.VocabularyDto;
import com.he181464.be_class.entity.Lesson;
import com.he181464.be_class.entity.Vocabulary;
import com.he181464.be_class.mapper.VocabMapper;
import com.he181464.be_class.repository.LessonRepository;
import com.he181464.be_class.repository.VocabRepository;
import com.he181464.be_class.service.VocabService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabServiceImpl implements VocabService {

    private final VocabRepository vocabRepository;
    private final VocabMapper vocabMapper;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public List<VocabularyDto> createListVocabulary(List<VocabularyDto> vocabularyCreateDtos) {
        List<Vocabulary> vocabularies = vocabularyCreateDtos.stream()
                .map(vocabMapper::toVocabularyEntity)
                .toList();

        List<Vocabulary> check = vocabRepository.saveAll(vocabularies);
        return check.stream()
                .map(vocabMapper::toVocabularyDto)
                .toList();
    }


    @Override
    public VocabularyDto updateVocabulary(VocabularyDto vocabularyDto) {
        if (vocabularyDto.getId() == null) {
            throw new IllegalArgumentException("Id khong duoc de trong");
        }
        Vocabulary existingVocabulary = vocabRepository.findById(vocabularyDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay vocabulary voi id: " + vocabularyDto.getId()));
        Lesson lesson = lessonRepository.findById(vocabularyDto.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException(("Khong tim thay lesson Id")));
        existingVocabulary.setEnglishWord(vocabularyDto.getEnglishWord());
        existingVocabulary.setVietnameseMeaning(vocabularyDto.getVietnameseMeaning());
        existingVocabulary.setExampleSentence(vocabularyDto.getExampleSample());
        existingVocabulary.setPronunciation(vocabularyDto.getPronunciation());
        existingVocabulary.setWordType(vocabularyDto.getWordType());
        existingVocabulary.setLesson(lesson);
        return vocabMapper.toVocabularyDto(vocabRepository.save(existingVocabulary));
    }


    @Override
    public void deleteVocabulary(Long id) {
        vocabRepository.deleteById(id);
    }

    @Override
    public VocabularyDto editVocab(VocabularyDto vocabularyDto) {
        vocabRepository.save(vocabMapper.toVocabularyEntity(vocabularyDto));
        return vocabularyDto;
    }


    @Override
    public List<VocabularyDto> getVocabulariesByLesson(Long id) {
        return vocabRepository.findByLessonId(id).stream()
                .map((item) -> vocabMapper.toVocabularyDto(item.orElseThrow())).toList();
    }

    @Override
    public List<VocabularyDto> importVocabFromExcelFile(MultipartFile file, Long lessonId) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

            // Bỏ qua dòng tiêu đề (header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setEnglishWord(row.getCell(0).getStringCellValue());
                vocabulary.setPronunciation(row.getCell(1).getStringCellValue());
                vocabulary.setVietnameseMeaning(row.getCell(2).getStringCellValue());
                vocabulary.setWordType(row.getCell(3).getStringCellValue());
                vocabulary.setExampleSentence(row.getCell(4).getStringCellValue());
                vocabulary.setLessonId(lessonId);
                vocabRepository.save(vocabulary);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }
    }


}
