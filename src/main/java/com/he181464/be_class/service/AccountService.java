package com.he181464.be_class.service;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.google.zxing.WriterException;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.model.response.TwoFAData;

import java.util.List;
import java.io.IOException;


public interface AccountService {

    boolean createAccount(AccountDto accountDto);

    List<AccountResponseDto> getAllAccount();


    Account getAccountByEmailToGenerate2Fa(String email);

    String get2FaSecretByEmail(String email);

    TwoFAData generate2FA(String email, String issuer) throws WriterException, IOException;

    boolean verifyCode(String secret, int code);

    AccountDto saveAccountSecretKey(Account account);
}
