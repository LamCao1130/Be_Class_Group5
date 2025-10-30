package com.he181464.be_class.service.serviceImpl;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.entity.Role;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.repository.AccountRepository;

import com.he181464.be_class.repository.ClassRoomStudentRepository;
import com.he181464.be_class.repository.RoleRepository;
import com.he181464.be_class.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClassRoomStudentRepository classRoomStudentRepository;

    @Override
    public Page<AccountResponseDto> getAllTeacherAccount(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());
        Page<Account> accountPage = accountRepository.findByRole(2,pageable);
        return accountPage.map(accountMapper::toDTO);
    }

    @Override
    public AccountResponseDto createAccountByAdmin(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(null);
        Role role = roleRepository.findById(accountDto.getRoleId())
                .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" + accountDto.getRoleId()));
        account.setRole(role);
        account.setRoleId(accountDto.getRoleId());
        account.setStatus(1);
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
    public AccountResponseDto editAccount(Long accountId, AccountDto accountDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("khong tim thay accound id" +accountId));
        accountMapper.updateEntityFromDTO(accountDto,account);
        if(StringUtils.hasText(accountDto.getPassword())){
            account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        }
        account.setUpdatedAt(LocalDateTime.now());

        if(accountDto.getRoleId() != null){
            Role role = roleRepository.findById(accountDto.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("khong tim thay role id" + accountDto.getRoleId()));
            account.setRole(role);
            account.setRoleId(accountDto.getRoleId());
        }

        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public Page<AccountResponseDto> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").ascending());

        Page<Account> accountPage = accountRepository.findByRole(1, pageable);

        List<Long> accountID = accountPage.stream().map(Account::getId).toList();
        Map<Long, Long> classCountMap = new HashMap<>();

        for (Long studentId : accountID) {
            Long count = classRoomStudentRepository.countClassRoomStudentByStudentId(studentId);
            classCountMap.put(studentId, count);
        }
        return accountPage.map(account -> {
            AccountResponseDto dto = accountMapper.toDTO(account);
            dto.setNumberClass(classCountMap.getOrDefault(account.getId(), 0L));
            return dto;
        });

    }
}
