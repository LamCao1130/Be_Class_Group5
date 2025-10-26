package com.he181464.be_class.service;

import com.he181464.be_class.dto.ClassRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassRoomService {

    ClassRoomDto createClassRoom(ClassRoomDto classRoomDto);

    ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto);

    void deleteClassRoom(Long id);

    Page<ClassRoomDto> searchClassRooms(int page, int size);

    List<ClassRoomDto> getClassRoomsByTeacherId(Long teacherId);

    ClassRoomDto getClassRoomsByClassroomId(Long id);
}
