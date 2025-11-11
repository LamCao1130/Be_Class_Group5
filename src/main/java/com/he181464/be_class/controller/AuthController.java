package com.he181464.be_class.controller;

import com.he181464.be_class.constant.AppConstant;
import com.he181464.be_class.dto.*;
import com.he181464.be_class.emailService.EmailService;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.jwt.JwtService;
import com.he181464.be_class.model.response.AuthResponse;
import com.he181464.be_class.model.response.TwoFAData;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.AccountService;
import com.he181464.be_class.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final EmailService emailService;

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
            if(account.getStatus() == 0){
                return ResponseEntity.status(403).body("Your account is disable");
            }
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

    @PostMapping("/v1/public/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenDto request) {
        String refreshToken = request.getRefreshToken();

        try {
            String username = jwtService.extractUsername(refreshToken);



            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isValidToken(refreshToken, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid refresh token");
            }

            // Sinh access token mới
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            tokenService.saveRefreshToken(userDetails.getUsername(), newRefreshToken);
            TokenDto tokenDto = new TokenDto(newAccessToken, newRefreshToken);
            return ResponseEntity.ok(tokenDto);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot refresh token: " + e.getMessage());
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

                if (accountService.verifyCode(verify2FADto.getBase32Code(), code)) {
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
                } else {
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
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account = optionalAccount.get();
        AccountDto dto = new AccountDto(
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
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account = optionalAccount.get();
        AccountDto updatedAccountDto = accountService.updateAccount(accountDto, account.getId());
        return ResponseEntity.ok(updatedAccountDto);
    }

    //Đổ mật khẩu
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordDTO changePasswordDTO) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Mật Khẩu Không Khớp");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account = optionalAccount.get();
        if (!accountService.passwordMatches(changePasswordDTO.getCurrentPassword(), account.getPassword())) {
            return ResponseEntity.badRequest().body("Mật Khẩu Hiện Tại Không Đúng");
        }
        accountService.changePassword(account, changePasswordDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/public/reset-password")
    public ResponseEntity<?> resetPasswordRequest(@RequestBody ResetPasswordRequestDto requestDto) {
        String newPass = emailService.sendMailResetPass(requestDto.getEmail());
        accountService.resetPassword(requestDto.getEmail(), newPass);
        return ResponseEntity.ok("Đã gửi mật khẩu mới đến email của bạn");
    }
}
