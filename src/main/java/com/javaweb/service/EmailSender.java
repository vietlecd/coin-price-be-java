package com.javaweb.service;


import com.javaweb.model.mongo_entity.userData;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSender {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email}")
    private String email;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
    }

    public void sendOtp(String mail) throws Exception {
        userData userdata = userRepository.findByEmail(mail);

        userdata.generateOtp(5, 5);
        userRepository.deleteByUsername(userdata.getUsername());
        userRepository.save(userdata);

        String emailBody = "<html>"
                + "<body>"
                + "<h2>Mã OTP là: "+ userdata.getOtp().getOtpCode() +"</h2>"
                + "<p></p>"
                + "</body>"
                + "</html>";

        sendEmail(userdata.getEmail(), "Yêu Cầu Cập Nhật Mật Khẩu", emailBody);
    }
}
