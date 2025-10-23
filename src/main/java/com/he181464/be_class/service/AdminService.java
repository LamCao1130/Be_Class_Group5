package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountRequestDto;
import com.he181464.be_class.dto.AccountResponseDto;

import java.util.List;

public interface AdminService {
    List<AccountResponseDto> getAllAccount();

    AccountResponseDto createAccountByAdmin(AccountRequestDto accountRequestDto);

    AccountResponseDto deleteAccount(Long accountId);

    AccountResponseDto editAccount(Long accountId, AccountRequestDto accountRequestDto);
}
