package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.dto.ClassRoomResponseDto;
import com.he181464.be_class.entity.ClassRoom;
import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.ClassRoomStudent;
import com.he181464.be_class.entity.ExamAttempts;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    Page<AccountResponseDto> getAllTeacherAccount(int page, int size);

    AccountResponseDto createAccountByAdmin(AccountDto accountDto);

    AccountResponseDto deleteAccount(Long accountId);

    AccountResponseDto editAccount(Long accountId, AccountDto accountDto);

    Page<AccountResponseDto> getAllStudents(int page, int size);

    AccountResponseDto getUserProfile(long studentId);

    Page<ExamAttemptsDTO> getExamAttemptsByStudentID(long studentId, int page, int size);

    Page<HomeworkSubmissionDTO> getHomeworkSubmissionsByStudentID(long studentId, int page, int size);

    Page<JoinedClassroom> getClassRoomStudentsByStudentID(long studentId, int page, int size);

    DashboardDTO getDashboard();

    List<LineChartDTO> getLineChart(int year);

    AccountResponseDto restoreAccount(Long accountId);

    AccountResponseDto getTeacherAccountAndClassesAndLessonAndExamById(Long accountId);

    Page<ClassRoomResponseDto> getAllClassRoom(int page,int size);

    ClassRoomResponseDto createClassRoom(ClassRoomDto classRoomDto);

    ClassRoomResponseDto editClassRoom(ClassRoomDto classRoomDto,Long classRoomId);

    ClassRoomResponseDto deleteClassRoom(Long classRoomId);

    ClassRoomResponseDto restoreClassRoom(Long classRoomId);

    ClassRoomResponseDto classDetail(Long classRoomId);
}
