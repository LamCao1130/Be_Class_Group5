package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.constant.AppConstant;
import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.entity.ClassRoom;
import com.he181464.be_class.repository.ClassRoomRepository;
import com.he181464.be_class.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassRoomServiceImpl implements ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    @Override
    @Transactional
    public ClassRoomDto createClassRoom(ClassRoomDto classRoomDto) {
        String codeCheck = generateCode();
        while (true){
            if(classRoomRepository.existsByCode(codeCheck)){
                codeCheck = generateCode();
            } else {
                break;
            }
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setName(classRoomDto.getName());
        classRoom.setTitle(classRoomDto.getTitle());
        classRoom.setCode(codeCheck);
        classRoom.setTeacherId(classRoomDto.getTeacherId());
        classRoom.setCreatedDate(LocalDateTime.now());
        classRoom.setStatus(AppConstant.STATUS_ACTIVE);
        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);
        classRoomDto.setId(savedClassRoom.getId());
        classRoomDto.setCreatedDate(savedClassRoom.getCreatedDate());
        return classRoomDto;
    }

    @Override
    public ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto) {
        if(classRoomDto.getId() == null) {
            throw new IllegalArgumentException("ID lớp học không được để trống");
        }
        ClassRoom classRoom = classRoomRepository.findById(classRoomDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Lớp học không tồn tại"));
        classRoom.setName(classRoomDto.getName());
        classRoom.setTitle(classRoomDto.getTitle());
        classRoom.setTeacherId(classRoomDto.getTeacherId());
        ClassRoom updatedClassRoom = classRoomRepository.save(classRoom);
        classRoomDto.setCreatedDate(updatedClassRoom.getCreatedDate());
        return classRoomDto;

    }

    @Override
    public void deleteClassRoom(Long id) {
        if(!classRoomRepository.existsById(id)) {
            throw new IllegalArgumentException("Lớp học không tồn tại");
        }
        ClassRoom classRoom = classRoomRepository.findById(id).get();
        classRoom.setStatus(AppConstant.STATUS_INACTIVE);
        classRoomRepository.save(classRoom);
    }

    @Override
    public Page<ClassRoomDto> searchClassRooms(int page, int size) {
        return null;
    }

    @Override
    public List<ClassRoomDto> getClassRoomsByTeacherId(Long teacherId) {
        return classRoomRepository.findByTeacherIdAndStatus(teacherId, AppConstant.STATUS_ACTIVE).stream().map(classRoom -> {
            ClassRoomDto dto = new ClassRoomDto();
            dto.setId(classRoom.getId());
            dto.setName(classRoom.getName());
            dto.setTitle(classRoom.getTitle());
            dto.setCode(classRoom.getCode());
            dto.setTeacherId(classRoom.getTeacherId());
            dto.setCreatedDate(classRoom.getCreatedDate());
            return dto;
        }).toList();
    }


    private  String generateCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 8).toUpperCase(); // VD: "A9F3D1B2"
    }

}
