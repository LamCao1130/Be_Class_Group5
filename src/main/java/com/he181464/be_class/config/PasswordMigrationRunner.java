package com.he181464.be_class.config;

import com.he181464.be_class.entity.Account;
import com.he181464.be_class.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PasswordMigrationRunner implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Account> accountList = accountRepository.findAll();
        for(Account account: accountList){
            String pass = account.getPassword();
            if(!pass.startsWith("$2a$")){
                account.setPassword(passwordEncoder.encode(pass));
                accountRepository.save(account);
            }
        }
    }
}
