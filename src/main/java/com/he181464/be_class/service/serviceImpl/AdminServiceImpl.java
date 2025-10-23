package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.AccountRequestDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.entity.Role;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.repository.RoleRepository;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public List<AccountResponseDto> getAllAccount() {
        return accountRepository.findAllByStatusTrue().stream().map(accountMapper::toDTO).toList();
    }

    @Override
    public AccountResponseDto createAccountByAdmin(AccountRequestDto accountRequestDto) {
        Account account = accountMapper.toEntity(accountRequestDto);
        account.setPassword(passwordEncoder.encode(accountRequestDto.getPassword()));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(null);
        Role role = roleRepository.findById(accountRequestDto.getRoleId())
                .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" +accountRequestDto.getRoleId()));
        account.setRole(role);
        account.setRoleId(accountRequestDto.getRoleId());

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDto deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        account.setStatus(0);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountResponseDto editAccount(Long accountId, AccountRequestDto accountRequestDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        accountMapper.updateEntityFromDTO(accountRequestDto,account);
        if(StringUtils.hasText(accountRequestDto.getPassword())){
            account.setPassword(passwordEncoder.encode(accountRequestDto.getPassword()));
        }
        account.setUpdatedAt(LocalDateTime.now());

        if(accountRequestDto.getRoleId() != null){
            Role role = roleRepository.findById(accountRequestDto.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" +accountRequestDto.getRoleId()));
            account.setRole(role);
            account.setRoleId(accountRequestDto.getRoleId());
        }

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }
}
