package com.he181464.be_class.controller;

import com.he181464.be_class.dto.*;
import com.he181464.be_class.entity.ExamAttempts;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/teacher")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<AccountResponseDto>> getAllAccountTeacher(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size){
        Page<AccountResponseDto> allAccountTeacher = adminService.getAllTeacherAccount(page,size);
        return ResponseEntity.ok(allAccountTeacher);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountDto accountDto){
        AccountResponseDto createdAccount = adminService.createAccountByAdmin(accountDto);
        return ResponseEntity.ok(createdAccount);
    }


    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> deleteAccount(@PathVariable("id") Long accountId){
        AccountResponseDto accountResponseDto = adminService.deleteAccount(accountId);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> editAccount(@PathVariable("id") Long accountId, @RequestBody AccountDto accountDto){
        AccountResponseDto accountResponseDto = adminService.editAccount(accountId, accountDto);
        return ResponseEntity.ok(accountResponseDto);
    }


    @GetMapping("/student")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<AccountResponseDto>> getAccountStudent(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size){
        Page<AccountResponseDto> allStudentAccount = adminService.getAllStudents(page,size);
        return ResponseEntity.ok(allStudentAccount);
    }

    @GetMapping("/student/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> getAccountStudent(@PathVariable("id") Long id){
        AccountResponseDto account = adminService.getUserProfile(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/student/{id}/examHistory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> examHistory(@PathVariable("id") Long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Page<ExamAttemptsDTO> examAttemptsDTOS = adminService.getExamAttemptsByStudentID(id,page,size );
        return ResponseEntity.ok(examAttemptsDTOS);
    }

    @GetMapping("/student/{id}/assignmentHistory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> assignmentHistory(@PathVariable("id") Long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Page<HomeworkSubmissionDTO> homeworkSubmissionDTOS = adminService.getHomeworkSubmissionsByStudentID(id,page,size );
        return ResponseEntity.ok(homeworkSubmissionDTOS);
    }


    @GetMapping("/student/{id}/classes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> joinedClasses(@PathVariable("id") Long id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Page<JoinedClassroom> classrooms = adminService.getClassRoomStudentsByStudentID(id,page,size );
        return ResponseEntity.ok(classrooms);
    }


    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> dashboard(){
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping("/lineChart")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> lineChart(@RequestParam("year")int year){
        return ResponseEntity.ok(adminService.getLineChart(year));
    }

    @PatchMapping("/restore/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> restoreAccount(@PathVariable("id") Long accountId){
        AccountResponseDto accountResponseDto = adminService.restoreAccount(accountId);
        return ResponseEntity.ok(accountResponseDto);
    }

    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> getTeacherAccountByAdmin(@PathVariable("id") Long accountId){
        AccountResponseDto accountResponseDto = adminService.getTeacherAccountAndClassesAndLessonAndExamById(accountId);
        return ResponseEntity.ok(accountResponseDto);
    }
}
