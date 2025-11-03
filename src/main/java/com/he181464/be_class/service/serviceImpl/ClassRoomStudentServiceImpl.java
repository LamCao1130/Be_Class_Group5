package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.dto.ClassRoomStudentDTO;
import com.he181464.be_class.entity.ClassRoom;
import com.he181464.be_class.entity.ClassRoomStudent;
import com.he181464.be_class.mapper.ClassRoomStudentMapper;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.repository.ClassRoomRepository;
import com.he181464.be_class.repository.ClassRoomStudentRepository;
import com.he181464.be_class.service.ClassRoomStudentDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassRoomStudentServiceImpl implements ClassRoomStudentDTOService {
    @Autowired
    private final ClassRoomRepository classRoomRepository;
    @Autowired
    private final ClassRoomStudentRepository classRoomStudentRepository;
    @Autowired
    private final ClassRoomStudentMapper classRoomStudentMapper;
    @Override
    public Page<ClassRoomStudentDTO> getClassRoomStudentsByStudentId(long accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return classRoomStudentRepository.findClassRoomByStudentId(accountId, pageable);
    }
}
