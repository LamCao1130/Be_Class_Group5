package com.he181464.be_class.controller;

import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponseDto>> getAllAccount(){
        List<AccountResponseDto> responseDtoList = accountService.getAllAccount();
        return ResponseEntity.ok(responseDtoList);
    }
}
