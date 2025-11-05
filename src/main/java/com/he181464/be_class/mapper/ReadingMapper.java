package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ReadingDto;
import com.he181464.be_class.entity.ReadingPassage;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface ReadingMapper {
    ReadingDto toReadingDto(ReadingPassage readingPassage);

    ReadingPassage toReadingPassageEntity(ReadingDto readingDto);
}
