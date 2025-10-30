package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import org.springframework.data.domain.Page;

public interface AdminService {
    Page<AccountResponseDto> getAllTeacherAccount(int page, int size);

    AccountResponseDto createAccountByAdmin(AccountDto accountDto);

    AccountResponseDto deleteAccount(Long accountId);

    AccountResponseDto editAccount(Long accountId, AccountDto accountDto);

    Page<AccountResponseDto> getAllStudents(int page, int size);


}
