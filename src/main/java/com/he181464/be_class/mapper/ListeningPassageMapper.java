package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ListeningPassageDto;
import com.he181464.be_class.entity.ListeningPassage;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)

public interface ListeningPassageMapper {
    ListeningPassageDto toListeningPassageDto(ListeningPassage listeningPassage);
    ListeningPassage toListeningPassageEntity(ListeningPassageDto listeningPassageDto);
}
