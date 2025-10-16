package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.VocabularyCreateDto;
import com.he181464.be_class.dto.VocabularyUpdateDto;
import com.he181464.be_class.entity.Vocabulary;
import com.he181464.be_class.model.response.VocabularyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VocabMapper {
    @Mapping(target="id",ignore = true)
    Vocabulary toVocabulary(VocabularyCreateDto vocabularyCreateDto);
    VocabularyResponse toVocabularyResponse(Vocabulary vocabulary);
    Vocabulary updateVocabulary(@MappingTarget Vocabulary vocabulary, VocabularyUpdateDto vocabularyUpdateDto);
}