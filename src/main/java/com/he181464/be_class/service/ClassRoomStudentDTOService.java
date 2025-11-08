package com.he181464.be_class.service;

import com.he181464.be_class.dto.ClassRoomStudentDTO;
import com.he181464.be_class.entity.ClassRoomStudent;
import org.springframework.data.domain.Page;

public interface ClassRoomStudentDTOService {
    Page<ClassRoomStudentDTO> getClassRoomStudentsByStudentId(long accountId, int page, int size);

    ClassRoomStudentDTO joinClassRoomByCode(long studentId, String code);

}
