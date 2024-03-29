package com.ll.medium.user.service;


import com.ll.medium.user.config.MailProperties;
import com.ll.medium.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public boolean checkEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Async
    public void sendVerificationEmail(String email, String token) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "utf-8");

        String htmlMsg = "<h3>Lcode 이메일 인증</h3>"
                + "<p>이메일 인증을 하시려면 아래 링크를 클릭해주세요 :</p>"
                + "<a href='https://api.llcode.me/api/v1/auth/confirm-account?token=" + token + "'>이메일 인증하기</a>";

        try {
            helper.setTo(email);
            helper.setSubject("Lcode 이메일 인증");
            helper.setText(htmlMsg, true); // true를 넣어주면 html 형식으로 메일이 보내집니다.
            helper.setFrom(mailProperties.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mail);
    }
}
