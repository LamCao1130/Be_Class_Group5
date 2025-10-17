package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.exception.ObjectExistingException;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean createAccount(AccountDto accountDto) {
        if(accountRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new ObjectExistingException("Email already exists");
        }
        Account account = new Account();
        account.setEmail(accountDto.getEmail());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setFullName(accountDto.getFullName());
        account.setPhoneNumber(accountDto.getPhoneNumber());
        account.setAddress(accountDto.getAddress());
        account.setRoleId(accountDto.getRoleId());
        account.setDateOfBirth(accountDto.getDateOfBirth());
        account.setStatus(accountDto.getStatus());
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account) != null;
    }
}
