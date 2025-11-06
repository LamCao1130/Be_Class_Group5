package com.he181464.be_class.service;

import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.dto.JoinClassroomDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassRoomService {

    ClassRoomDto createClassRoom(ClassRoomDto classRoomDto);

    ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto);

    void deleteClassRoom(Long id);

    Page<ClassRoomDto> searchClassRooms(int page, int size);

    List<ClassRoomDto> getClassRoomsByTeacherId(Long teacherId);

    ClassRoomDto getClassRoomById(Long id);

    void joinClassroom(JoinClassroomDto code);
}
