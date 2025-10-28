package com.he181464.be_class.controller;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountDto accountDto){
        AccountResponseDto createdAccount = adminService.createAccountByAdmin(accountDto);
        return ResponseEntity.ok(createdAccount);
    }

    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponseDto> deleteAccount(@PathVariable("id") Long accountId){
        AccountResponseDto accountResponseDto = adminService.deleteAccount(accountId);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponseDto> editAccount(@PathVariable("id") Long accountId, @RequestBody AccountDto accountDto){
        AccountResponseDto accountResponseDto = adminService.editAccount(accountId, accountDto);
        return ResponseEntity.ok(accountResponseDto);
    }
}
