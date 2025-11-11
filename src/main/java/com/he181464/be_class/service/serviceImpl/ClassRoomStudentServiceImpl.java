package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.constant.AppConstant;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassRoomStudentServiceImpl implements ClassRoomStudentDTOService {
    private final ClassRoomRepository classRoomRepository;

    private final ClassRoomStudentRepository classRoomStudentRepository;

    private final ClassRoomStudentMapper classRoomStudentMapper;

    @Override
    public Page<ClassRoomStudentDTO> getClassRoomStudentsByStudentId(long accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return classRoomStudentRepository.findClassRoomByStudentId(accountId, pageable);
    }

    @Override
    public ClassRoomStudentDTO joinClassRoomByCode(long studentId, String code) {
        ClassRoom classRoom = classRoomRepository.findByCodeAndStatus(code, AppConstant.STATUS_ACTIVE);
        if(classRoom!= null){
            if(classRoomStudentRepository.findByClassRoomIdAndStudentId(classRoom.getId(), studentId) != null){
                throw new IllegalArgumentException("Học sinh đã tham gia lớp học này");
            }
            ClassRoomStudent classRoomStudent = new ClassRoomStudent();
            classRoomStudent.setClassRoomId(classRoom.getId());
            classRoomStudent.setStudentId(studentId);
            classRoomStudent.setJoinDate(LocalDateTime.now());
            ClassRoomStudent saved = classRoomStudentRepository.save(classRoomStudent);
            return classRoomStudentMapper.toDTO(saved);
        }
        else{
            throw new IllegalArgumentException("Mã lớp học không tồn tại");
        }
    }


}
