package com.he181464.be_class.controller;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.LoginDto;
import com.he181464.be_class.dto.Verify2FADto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.jwt.JwtService;
import com.he181464.be_class.model.response.AuthResponse;
import com.he181464.be_class.model.response.TwoFAData;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.AccountService;
import com.he181464.be_class.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final AccountService accountService;

    private final JwtService jwtService;

    private final TokenService tokenService;
    private final AccountRepository accountRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountDto registerRequest) {
        boolean isRegistered = accountService.createAccount(registerRequest);
        if (isRegistered) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            Account account = accountService.getAccountByEmailToGenerate2Fa(authRequest.getEmail());
            if (account.getSecretCode() == null || account.getSecretCode().isEmpty()) {
                // 2FA not enabled, proceed with normal login
                String issuer = "CAOLAM_API";
                String email = authRequest.getEmail();
                TwoFAData twoFAData = accountService.generate2FA(email, issuer);
                return ResponseEntity.ok(twoFAData);
            } else {
                // 2FA is enabled, prompt for 2FA verification
                return ResponseEntity.status(206).body("2FA verification required");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed");
        }

    }

    @PostMapping("/v1/public/2fa-verify")
    public ResponseEntity<?> verify2FA(@RequestBody Verify2FADto verify2FADto) {
        try {
            Account accountCheck = accountService.getAccountByEmailToGenerate2Fa(verify2FADto.getEmail());
            if (accountCheck.getSecretCode() == null || accountCheck.getSecretCode().trim().isEmpty()) {
                // Setup 2FA for the first time
                Account account = accountService.getAccountByEmailToGenerate2Fa(verify2FADto.getEmail());
                account.setSecretCode(verify2FADto.getBase32Code());
                Integer code = Integer.parseInt(verify2FADto.getSecretCode());

                if(accountService.verifyCode(verify2FADto.getBase32Code(), code)){
                    account.setSecretCode(verify2FADto.getBase32Code());
                    accountService.saveAccountSecretKey(account);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(account.getEmail());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String accessToken = jwtService.generateAccessToken(userDetails);
                    String refreshToken = jwtService.generateRefreshToken(userDetails);
                    tokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);
                    return ResponseEntity
                            .ok()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .body(AuthResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .build());
                }
                else{
                    return ResponseEntity.badRequest().body("Invalid 2FA code during setup");
                }

            } else {
                Integer code = Integer.parseInt(verify2FADto.getSecretCode());
                Account account = accountService.getAccountByEmailToGenerate2Fa(verify2FADto.getEmail());
                boolean isCodeValid = accountService.verifyCode(account.getSecretCode(), code);
                if (isCodeValid) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(account.getEmail());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String accessToken = jwtService.generateAccessToken(userDetails);
                    String refreshToken = jwtService.generateRefreshToken(userDetails);
                    tokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);
                    return ResponseEntity
                            .ok()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .body(AuthResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .build());
                } else {
                    return ResponseEntity.badRequest().body("Invalid 2FA code");
                }
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("2FA verification failed");
        }

    }

// Profile account student
    @GetMapping("/profileStudent")
    public ResponseEntity<AccountDto> getProfileStudent(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount=accountRepository.findByEmail(email);
        if(optionalAccount.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Account account=optionalAccount.get();
        AccountDto dto=new AccountDto(
                account.getEmail(),
                account.getFullName(),
                account.getPassword(),
                account.getRoleId(),
                account.getPhoneNumber(),
                account.getAddress(),
                account.getDateOfBirth(),
                account.getStatus()
        );
        return ResponseEntity.ok(dto);
    }

    //cập nhật Profile
    @PutMapping("/profileStudent")
    public ResponseEntity<AccountDto> updateProfileStudent(Authentication authentication, @RequestBody AccountDto accountDto) {
        if(authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount=accountRepository.findByEmail(email);
        if(optionalAccount.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Account account=optionalAccount.get();
        AccountDto updatedAccountDto=accountService.updateAccount(accountDto,account.getId());
        return ResponseEntity.ok(updatedAccountDto);
    }
}
