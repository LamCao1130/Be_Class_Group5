package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ClassRoomStudentDTO;
import com.he181464.be_class.entity.ClassRoomStudent;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface ClassRoomStudentMapper {
    ClassRoomStudentDTO toDTO(ClassRoomStudent classRoomStudent);
    ClassRoomStudent toEntity(ClassRoomStudentDTO classRoomStudentDTO);
}
