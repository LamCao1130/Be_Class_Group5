package com.he181464.be_class.emailService;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public String sendMailResetPass(String to) {
        String code = generateCode();
        String fullText =  "\nMã xác nhận của bạn là: " + code + "\nVui lòng không chia sẻ mã này với bất kỳ ai.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lamch301104@gmail.com");
        message.setTo(to);
        message.setSubject("Yêu cầu đặt lại mật khẩu");
        message.setText(fullText);
        mailSender.send(message);
        return code;
    }

    private String generateCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 8).toUpperCase(); // VD: "A9F3D1B2"
    }
}
