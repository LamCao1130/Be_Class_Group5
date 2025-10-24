package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.entity.ClassRoom;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface ClassRoomMapper {

    ClassRoomDto toClassRoomDto(ClassRoom classRoom);

    ClassRoom toClassRoomEntity(ClassRoomDto classRoomDto);

}
