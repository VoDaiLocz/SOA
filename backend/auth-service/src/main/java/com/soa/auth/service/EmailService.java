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

    public void sendPasswordResetOtpEmail(String toEmail, String hoTen, String otp) {
        if (mailSender == null || smtpUsername == null || smtpUsername.isBlank()) {
            log.warn("==================================================================");
            log.warn(">>> [DEV/TEST MODE] CHƯA CẤU HÌNH SMTP GMAIL (SPRING_MAIL_USERNAME)");
            log.warn(">>> MÃ XÁC THỰC OTP ĐẶT LẠI MẬT KHẨU CHO {}: [{}]", toEmail, otp);
            log.warn("==================================================================");
            return;
        }

        String subject = "[Đại học Quy Nhơn] Mã xác nhận đặt lại mật khẩu NCKH";
        String content = String.format(
                "Kính gửi %s,\n\n" +
                "Hệ thống Quản lý Đề tài NCKH & Đồ án Tốt nghiệp - Trường Đại học Quy Nhơn nhận được yêu cầu đặt lại mật khẩu cho tài khoản liên kết với email này.\n\n" +
                "Mã xác nhận (OTP) của bạn là: %s\n" +
                "Mã có hiệu lực trong vòng 15 phút. Tuyệt đối không chia sẻ mã này cho người khác.\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này — mật khẩu hiện tại của bạn vẫn an toàn.\n\n" +
                "Trân trọng,\n" +
                "Phòng Khoa học Công nghệ & Hợp tác Quốc tế - Trường Đại học Quy Nhơn",
                hoTen != null ? hoTen : "Quý Thầy/Cô và Bạn",
                otp
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpUsername);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Đã gửi email OTP đặt lại mật khẩu tới: {}", toEmail);
        } catch (Exception e) {
            log.error("Lỗi gửi email SMTP tới {}: {}", toEmail, e.getMessage());
            throw new IllegalStateException("Không thể gửi email qua SMTP: " + e.getMessage(), e);
        }
    }
}
