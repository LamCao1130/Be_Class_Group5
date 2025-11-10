package com.he181464.be_class.controller;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.dto.ClassRoomResponseDto;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
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

    @PostMapping("/teacher/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountDto accountDto){
        AccountResponseDto createdAccount = adminService.createAccountByAdmin(accountDto);
        return ResponseEntity.ok(createdAccount);
    }

    @PatchMapping("/teacher/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> deleteAccount(@PathVariable("id") Long accountId){
        AccountResponseDto accountResponseDto = adminService.deleteAccount(accountId);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PutMapping("/teacher/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AccountResponseDto> editAccount(@PathVariable("id") Long accountId, @RequestBody AccountDto accountDto){
        AccountResponseDto accountResponseDto = adminService.editAccount(accountId, accountDto);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PatchMapping("/teacher/restore/{id}")
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

    @GetMapping("/classroom")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ClassRoomResponseDto>> getAllClassRoom(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size){
        Page<ClassRoomResponseDto> dtos =adminService.getAllClassRoom(page,size);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/classroom/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClassRoomResponseDto> createClassRoom(@RequestBody ClassRoomDto classRoomDto){
        ClassRoomResponseDto dto = adminService.createClassRoom(classRoomDto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/classroom/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClassRoomResponseDto> editClassRoom(@RequestBody ClassRoomDto classRoomDto, @PathVariable("id") Long classId){
        ClassRoomResponseDto dto = adminService.editClassRoom(classRoomDto,classId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/classroom/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClassRoomResponseDto> deleteClassRoom(@PathVariable("id") Long classId){
        ClassRoomResponseDto dto = adminService.deleteClassRoom(classId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/classroom/restore/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClassRoomResponseDto> restoreClassRoom(@PathVariable("id") Long classId){
        ClassRoomResponseDto dto = adminService.restoreClassRoom(classId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/classroom/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClassRoomResponseDto> classDetail(@PathVariable("id") Long classId){
        ClassRoomResponseDto dto = adminService.classDetail(classId);
        return ResponseEntity.ok(dto);
    }
}
