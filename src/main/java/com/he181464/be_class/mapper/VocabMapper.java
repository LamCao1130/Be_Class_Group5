package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.VocabularyDto;
import com.he181464.be_class.entity.Vocabulary;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface VocabMapper {
    VocabularyDto toVocabularyDto(Vocabulary vocabulary);

    Vocabulary toVocabularyEntity(VocabularyDto vocabularyDto);

}