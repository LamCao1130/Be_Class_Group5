package com.he181464.be_class.service.serviceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.exception.ObjectExistingException;
import com.he181464.be_class.mapper.AccountMapper;
import com.he181464.be_class.model.response.TwoFAData;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.AccountService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public boolean createAccount(AccountDto accountDto) {
        if (accountRepository.findByEmail(accountDto.getEmail()).isPresent()) {
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
        account.setStatus(1);
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account) != null;
    }

    @Override
    public Account getAccountByEmailToGenerate2Fa(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account;
    }

    @Override
    public String get2FaSecretByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.getSecretCode();
    }


    @Override
    public TwoFAData generate2FA(String email, String issuer) throws WriterException, IOException {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        String secret = key.getKey();

        String otpAuthUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, email, secret, issuer
        );

        // Tạo QR code base64
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        return new TwoFAData(secret, base64, otpAuthUrl);
    }

    @Override
    public boolean verifyCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }

    @Override
    public AccountDto saveAccountSecretKey(Account account) {
        return accountMapper.toAccountDto(accountRepository.save(account));
    }

}
