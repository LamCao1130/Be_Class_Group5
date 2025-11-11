package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.constant.AppConstant;
import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.*;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.mapper.ClassRoomMapper;
import com.he181464.be_class.mapper.ExamMapper;
import com.he181464.be_class.mapper.LessonMapper;
import com.he181464.be_class.repository.*;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClassRoomRepository classRoomRepository;
    private final TokenRepository tokenRepository;
    private final ClassRoomMapper classRoomMapper;
    private final ClassRoomStudentRepository classRoomStudentRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;


    @Override
    public Page<AccountResponseDto> getAllTeacherAccount(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        Page<Account> accountPage = accountRepository.findByRole(2,pageable);
        return accountPage.map(account -> {
            AccountResponseDto dto = accountMapper.toDTO(account);
            List<Long> classRoomIds = classRoomRepository.findByTeacherIdAndStatus(account.getId(),AppConstant.STATUS_ACTIVE)
                    .stream().map(ClassRoom::getId).toList();
            dto.setClassRoomIds(classRoomIds);

            return dto;
        });
    }

    @Override
    public AccountResponseDto createAccountByAdmin(AccountDto accountDto) {
        if(accountRepository.existsAccountByEmail(accountDto.getEmail())){
            throw new DuplicateKeyException("Email already exist");
        }
        if(accountRepository.existsAccountByPhoneNumber(accountDto.getPhoneNumber())){
            throw new DuplicateKeyException("Phone number already exist");
        }
        Account account = accountMapper.toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getFullName() + "123"));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(null);
        Role role = roleRepository.findById(accountDto.getRoleId())
                .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" + accountDto.getRoleId()));
        account.setRole(role);
        account.setRoleId(accountDto.getRoleId());
        account.setStatus(Integer.parseInt(AppConstant.STATUS_ACTIVE));

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDto deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Not found account id" +accountId));
        boolean isTeaching = classRoomRepository.existsByTeacherId(accountId);
        if (isTeaching) {
            throw new IllegalStateException("Cannot delete this account because the teacher is assigned to a class.");
        }

        account.setStatus(Integer.parseInt(AppConstant.STATUS_INACTIVE));

        List<Token> allValidToken = tokenRepository.findAllValidTokenByAccountId(accountId);
        allValidToken.forEach(t ->
                {
                    t.setRevoked(1);
                    t.setExpired(1);
                }
        );
        tokenRepository.saveAll(allValidToken);
        String unique = "_DELETED_" + accountId;
        account.setEmail(account.getEmail()+unique);
        account.setPhoneNumber(account.getPhoneNumber()+unique);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }


    @Override
    public AccountResponseDto restoreAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Not found account id"+accountId));
        account.setStatus(Integer.parseInt(AppConstant.STATUS_ACTIVE));
        String unique = "_DELETED_"+ accountId;

        String currentEmail = account.getEmail();
        int suffixEmailIndex = currentEmail.lastIndexOf(unique);
        if(suffixEmailIndex == -1){
            throw new IllegalArgumentException("Not found suffix in email");
        }
        String originalEmail = currentEmail.substring(0,suffixEmailIndex);
        account.setEmail(originalEmail);

        String currentPhone = account.getPhoneNumber();
        int suffixPhoneIndex = currentPhone.lastIndexOf(unique);
        if(suffixPhoneIndex == -1){
            throw new IllegalArgumentException("Not found suffix in phone");
        }
        String phoneOriginal = currentPhone.substring(0,suffixPhoneIndex);
        account.setPhoneNumber(phoneOriginal);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDto getTeacherAccountAndClassesAndLessonAndExamById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Not found account id"+accountId));
        List<ClassRoom> classRoom = classRoomRepository.findByTeacherIdAndStatus(accountId,AppConstant.STATUS_ACTIVE);

        List<Long> classIds = classRoom.stream().map(ClassRoom::getId).toList();
        List<Object[]> studentCount = classRoomStudentRepository.countStudentByListClassRoomId(classIds);
        Map<Long,Integer> studentCountMap = studentCount.stream().collect(Collectors.toMap(
                array -> (Long) array[0],
                array -> ((Long) array[1]).intValue()
        ));
        List<LessonDto> lessonDtos = lessonRepository.findAllByTeacherId(accountId).stream().map(lessonMapper::toLessonDto).toList();
        Map<Long,List<LessonDto>> lessonByClass = lessonDtos.stream().collect(Collectors.groupingBy(LessonDto::getClassRoomId));

        List<ExamDto> examDtos = examRepository.findAllByTeacherId(accountId).stream().map(examMapper::toDTO).toList();
        Map<Long, List<ExamDto>> examByClass = examDtos.stream()
                .filter(exam -> exam.getClassRoomId() != null)
                .collect(Collectors.groupingBy(ExamDto::getClassRoomId));


        List<ClassRoomResponseDto> classRoomResponseDtos = classRoom.stream().map(
                clr -> {
                    ClassRoomResponseDto dto = classRoomMapper.toDTO(clr);
                    Integer studentCounts = studentCountMap.getOrDefault(clr.getId(),0);
                    dto.setStudentCount(studentCounts);
                    dto.setLessons(lessonByClass.getOrDefault(clr.getId(),new ArrayList<>()));
                    dto.setExams(examByClass.getOrDefault(clr.getId(),new ArrayList<>()));
                    return dto;
                }
        ).toList();

        AccountResponseDto accountResponseDto = accountMapper.toDTO(account);
        accountResponseDto.setClassRooms(classRoomResponseDtos);
        return accountResponseDto;
    }



    @Override
    public AccountResponseDto editAccount(Long accountId, AccountDto accountDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        if(accountRepository.existsAccountByEmailAndIdIsNot(accountDto.getEmail(),accountId)){
            throw new DuplicateKeyException("Email already exist");
        }
        if(accountRepository.existsAccountByPhoneNumberAndIdIsNot(accountDto.getPhoneNumber(),accountId)){
            throw new DuplicateKeyException("Phone number already exist");
        }
        accountMapper.updateEntityFromDTO(accountDto,account);
        account.setUpdatedAt(LocalDateTime.now());

        if(accountDto.getRoleId() != null){
            Role role = roleRepository.findById(accountDto.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" + accountDto.getRoleId()));
            account.setRole(role);
        }

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }


    @Override
    public Page<ClassRoomResponseDto> getAllClassRoom(int page,int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("id").ascending());
        Page<ClassRoom> classRooms = classRoomRepository.findAll(pageable);
        List<Object[]> studentCount = classRoomStudentRepository.countStudentByListClassRoomId(classRooms.stream().map(ClassRoom::getId).toList());
        Map<Long,Integer> countStudentByClassRoom = studentCount.stream().collect(Collectors.toMap(
                array -> (Long) array[0],
                array -> ((Long) array[1]).intValue()
        ));
        return classRooms.map(cr -> {
           ClassRoomResponseDto dto = classRoomMapper.toDTO(cr);
           Integer studentCounts = countStudentByClassRoom.getOrDefault(cr.getId(),0);
           dto.setStudentCount(studentCounts);
           return dto;
        });
    }

    @Override
    public ClassRoomResponseDto createClassRoom(ClassRoomDto classRoomDto) {
        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDto);
        String code;
        do {
            code = generateCode();
        } while (classRoomRepository.existsByCode(code));

        Account account = accountRepository.findById(classRoomDto.getTeacherId())
                        .orElseThrow(() -> new NoSuchElementException("khong tim thay account id"));
        classRoom.setTeacher(account);
        classRoom.setCode(code);
        classRoom.setCreatedDate(LocalDateTime.now());
        classRoomRepository.save(classRoom);
        ClassRoomResponseDto dto = classRoomMapper.toDTO(classRoom);
        dto.setStudentCount(0);

        return dto;
    }

    @Override
    public ClassRoomResponseDto editClassRoom(ClassRoomDto classRoomDto, Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new NoSuchElementException("Khong tim thay classroom"));
        classRoomMapper.updateEntityFromDto(classRoom,classRoomDto);
        Integer studentCount = classRoomStudentRepository.countStudentByClassRoomId(classRoomId);
        log.info(studentCount.toString());
        Account account = accountRepository.findById(classRoomDto.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("khong tim thay account id"));
        classRoom.setTeacher(account);

        classRoomRepository.save(classRoom);
        ClassRoomResponseDto dto = classRoomMapper.toDTO(classRoom);
        dto.setStudentCount(studentCount);
        return dto;

    }

    @Override
    public ClassRoomResponseDto deleteClassRoom(Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new NoSuchElementException("Khong tim thay class"));
        Integer countStudent = classRoomStudentRepository.countStudentByClassRoomId(classRoomId);
        if(countStudent > 0){
            throw new IllegalStateException("Class have students. Can not deactivate");
        }
        classRoom.setStatus(AppConstant.STATUS_INACTIVE);
        classRoomRepository.save(classRoom);
        return classRoomMapper.toDTO(classRoom);
    }

    @Override
    public ClassRoomResponseDto restoreClassRoom(Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new NoSuchElementException("Khong tim thay class"));
        classRoom.setStatus(AppConstant.STATUS_ACTIVE);
        classRoomRepository.save(classRoom);
        ClassRoomResponseDto dto = classRoomMapper.toDTO(classRoom);
        dto.setStudentCount(0);
        return dto;
    }

    @Override
    public ClassRoomResponseDto classDetail(Long classRoomId) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy class ID: " + classRoomId));

        ClassRoomResponseDto dto = classRoomMapper.toDTO(classRoom);
        List<ClassRoomStudent> classRoomStudents = classRoomStudentRepository.findByClassRoomId(classRoomId);
        List<Long> studentIds = classRoomStudents.stream()
                .map(ClassRoomStudent::getStudentId)
                .toList();

        List<Account> students = accountRepository.findAllById(studentIds);
        List<AccountResponseDto> studentDtos = students.stream()
                .map(accountMapper::toDTO)
                .toList();
        dto.setStudents(studentDtos);
        dto.setStudentCount(studentDtos.size());

        List<LessonDto> lessons = lessonRepository.findAllByClassRoomId(classRoomId)
                .stream()
                .map(lessonMapper::toLessonDto)
                .toList();
        dto.setLessons(lessons);

        List<ExamDto> exams = examRepository.findAllByClassRoomId(classRoomId)
                .stream()
                .map(examMapper::toDTO)
                .toList();
        dto.setExams(exams);

        return dto;
    }

    private String generateCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 8).toUpperCase(); // VD: "A9F3D1B2"
    }
}
