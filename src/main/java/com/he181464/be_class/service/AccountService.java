package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountRequestDto;
import com.he181464.be_class.dto.AccountResponseDto;

import java.util.List;


public interface AccountService {

    boolean createAccount(AccountRequestDto accountDto);

    List<AccountResponseDto> getAllAccount();


}
