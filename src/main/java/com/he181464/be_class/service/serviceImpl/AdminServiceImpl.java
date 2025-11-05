package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.constant.AppConstant;
import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.*;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.mapper.ExamAttemptMapper;
import com.he181464.be_class.mapper.HomeworkSubmissionMapper;
import com.he181464.be_class.repository.*;

import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ExamAttemptMapper examAttemptMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClassRoomStudentRepository classRoomStudentRepository;
    private final ExamAttemptsRepository examAttemptsRepository;
    private final HomeworkSubmissionRepository homeworkSubmissionRepository;
    private final HomeworkSubmissionMapper homeworkSubmissionMapper;
    private final ClassRoomRepository classRoomRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Page<AccountResponseDto> getAllTeacherAccount(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        Page<Account> accountPage = accountRepository.findByRole(2,pageable);
        return accountPage.map(accountMapper::toDTO);
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
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        account.setStatus(0);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDto editAccount(Long accountId, AccountDto accountDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        accountMapper.updateEntityFromDTO(accountDto,account);
        if(StringUtils.hasText(accountDto.getPassword())){
            account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        }
        account.setUpdatedAt(LocalDateTime.now());

        if(accountDto.getRoleId() != null){
            Role role = roleRepository.findById(accountDto.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" + accountDto.getRoleId()));
            account.setRole(role);
            account.setRoleId(accountDto.getRoleId());
        }

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public Page<AccountResponseDto> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());

        Page<Account> accountPage = accountRepository.findByRole(1, pageable);

        List<Long> accountID = accountPage.stream().map(Account::getId).toList();
        Map<Long, Long> classCountMap = new HashMap<>();

        for (Long studentId : accountID) {
            Long count = classRoomStudentRepository.countClassRoomStudentByStudentId(studentId);
            classCountMap.put(studentId, count);
        }
        return accountPage.map(account -> {
            AccountResponseDto dto = accountMapper.toDTO(account);
            dto.setNumberClass(classCountMap.getOrDefault(account.getId(), 0L));
            return dto;
        });

    }

    @Override
    public AccountResponseDto getUserProfile(long studentId) {
        Account account = accountRepository.findById(studentId).orElseThrow(() ->  new NoSuchElementException("khong tim thay accound id" +studentId));
        AccountResponseDto dto = accountMapper.toDTO(account);
        return dto;
    }

    @Override
    public Page<ExamAttemptsDTO> getExamAttemptsByStudentID(long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ExamAttempts> listExam = examAttemptsRepository.findExamAttemptsByStudentId(studentId, pageable);

        return listExam.map(examAttemptMapper ::toExamAttemptsDTO);

    }

    @Override
    public Page<HomeworkSubmissionDTO> getHomeworkSubmissionsByStudentID(long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HomeworkSubmissions> listHomework = homeworkSubmissionRepository.findHomeworkSubmissionByStudentId(studentId, pageable);

        return listHomework.map(entity -> {
            HomeworkSubmissionDTO dto = homeworkSubmissionMapper.homeworkSubmissionToDto(entity);
            if (entity.getGradedBy() != null) {
                accountRepository.findById((long) entity.getGradedBy())
                        .ifPresent(acc -> dto.setGradedBy(acc.getFullName()));
            } else {
                dto.setGradedBy("—");
            }
            return dto;
        });
    }

    @Override
    public Page<JoinedClassroom> getClassRoomStudentsByStudentID(long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassRoomStudent> listClassrooms = classRoomStudentRepository.findClassRoomStudentByStudentId(studentId, pageable);

        return listClassrooms.map(ClassRoomStudent -> {
            ClassRoom classRoom = classRoomRepository.findClassRoomById(ClassRoomStudent.getClassRoomId());
            Account account = accountRepository.findAccountsById(classRoom.getTeacherId());
            JoinedClassroom classroomDTO = new JoinedClassroom();
            classroomDTO.setClassroomId(ClassRoomStudent.getClassRoomId());
            classroomDTO.setClassroomName(classRoom.getName());
            classroomDTO.setStudentId(studentId);
            classroomDTO.setJoinedDate(ClassRoomStudent.getJoinDate());
            classroomDTO.setTeacherName(account.getFullName());

            return classroomDTO;
        });
    }

    @Override
    public DashboardDTO getDashboard() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalActiveClasses(classRoomRepository.countTotalActiveClassRoom());
        dashboardDTO.setTotalActiveStudents(accountRepository.countActiveStudent());
        dashboardDTO.setTotalActiveLessons(lessonRepository.countActiveLessons());
        dashboardDTO.setTotalActiveTeachers(accountRepository.countActiveTeacher());
        return dashboardDTO;
    }

    @Override
    public List<LineChartDTO> getLineChart(int year) {

        List<Object[]> studentData = accountRepository.getStudentByYear(year);
        List<Object[]> classData = classRoomRepository.getClassRoomByYear(year);

        Map<Integer, LineChartDTO> map = new HashMap<>();

        for (int m = 1; m <= 12; m++) {
            map.put(m, new LineChartDTO(String.valueOf(m), 0, 0));
        }


        for (Object[] row : studentData) {
            int month = ((Number) row[0]).intValue();
            int total = ((Number) row[1]).intValue();

            LineChartDTO dto = map.get(month);
            dto.setStudent(total);
        }


        for (Object[] row : classData) {
            int month = ((Number) row[0]).intValue();
            int total = ((Number) row[1]).intValue();

            LineChartDTO dto = map.get(month);
            dto.setClassroom(total);
        }


        return map.values()
                .stream()
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.getMonth())))
                .collect(Collectors.toList());
    }


}
