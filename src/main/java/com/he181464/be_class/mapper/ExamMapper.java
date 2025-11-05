package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.ExamDto;
import com.he181464.be_class.entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface ExamMapper {

    @Mapping(target = "classRoomId",source = "classRoom.id")
    @Mapping(source = "classRoom.name", target = "classRoomName")
    @Mapping(source = "account.id", target = "createdBy")
    @Mapping(source = "account.fullName", target = "creatorName")
    ExamDto toDTO(Exam exam);
    Exam toEntity(ExamDto examDto);
}
