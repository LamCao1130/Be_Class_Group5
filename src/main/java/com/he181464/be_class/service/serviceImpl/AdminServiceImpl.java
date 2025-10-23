package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountResponseDto> getAllAccount() {
        return accountRepository.findAll().stream().map(accountMapper::toDTO).toList();
    }
}
