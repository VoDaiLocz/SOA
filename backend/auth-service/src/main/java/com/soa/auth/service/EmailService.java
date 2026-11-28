package com.soa.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String smtpUsername;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String hoTen, String temporaryPassword) {
        if (mailSender == null || smtpUsername == null || smtpUsername.isBlank()) {
            throw new IllegalStateException(
                    "Chưa cấu hình tài khoản SMTP (SPRING_MAIL_USERNAME / SPRING_MAIL_PASSWORD) để gửi email thật"
            );
        }

        String subject = "[Đại học Quy Nhơn] Cấp lại mật khẩu tài khoản NCKH";
        String content = String.format(
                "Kính gửi %s,\n\n" +
                "Hệ thống Quản lý Đề tài Nghiên cứu Khoa học & Đồ án Tốt nghiệp - Trường Đại học Quy Nhơn nhận được yêu cầu cấp lại mật khẩu cho tài khoản liên kết với email này.\n\n" +
                "Mật khẩu tạm thời mới của bạn là: %s\n\n" +
                "Vui lòng đăng nhập vào hệ thống tại http://localhost:4200 và thực hiện đổi mật khẩu ngay để đảm bảo an toàn thông tin.\n\n" +
                "Trân trọng,\n" +
                "Phòng Khoa học Công nghệ & Hợp tác Quốc tế - Trường Đại học Quy Nhơn\n" +
                "Số 170 An Dương Vương, TP. Quy Nhơn, Bình Định",
                hoTen != null ? hoTen : "Quý Thầy/Cô và Bạn",
                temporaryPassword
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpUsername);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Đã gửi email khôi phục mật khẩu thành công tới: {}", toEmail);
        } catch (Exception e) {
            log.error("Lỗi gửi email SMTP tới {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Không thể gửi email qua SMTP: " + e.getMessage(), e);
        }
    }
}
