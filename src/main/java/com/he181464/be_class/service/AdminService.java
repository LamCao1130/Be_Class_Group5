package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;

import java.util.List;

public interface AdminService {
    List<AccountResponseDto> getAllAccount();

    AccountResponseDto createAccountByAdmin(AccountDto accountDto);

    AccountResponseDto deleteAccount(Long accountId);

    AccountResponseDto editAccount(Long accountId, AccountDto accountDto);
}
