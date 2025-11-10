package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.GrammarPointDto;
import com.he181464.be_class.entity.GrammarPoint;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface GrammarPointMapper {
    GrammarPointDto toGrammarPointDto(GrammarPoint grammarPoint);

    GrammarPoint toGrammarPointEntity(GrammarPointDto grammarPointDto);
}
