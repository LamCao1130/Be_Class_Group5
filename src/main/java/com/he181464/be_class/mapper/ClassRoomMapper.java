package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.dto.ClassRoomResponseDto;
import com.he181464.be_class.entity.ClassRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfig.class)
public interface ClassRoomMapper {
    @Mapping(target = "teacherName", source = "teacher.fullName")
    @Mapping(target = "teacherId", source = "teacherId")
    ClassRoomResponseDto toDTO(ClassRoom classRoom);

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "teacher",ignore = true)
    @Mapping(target = "teacherId",source = "teacherId")
    ClassRoom toEntity(ClassRoomDto classRoomDto);

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "teacher",ignore = true)
    @Mapping(target = "teacherId",source = "teacherId")
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "createdDate",ignore = true)
    void updateEntityFromDto(@MappingTarget ClassRoom classRoom, ClassRoomDto classRoomDto);

    ClassRoomDto toClassRoomDto(ClassRoom classRoom);

    ClassRoom toClassRoomEntity(ClassRoomDto classRoomDto);

}
