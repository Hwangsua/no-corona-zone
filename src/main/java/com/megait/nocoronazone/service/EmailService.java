package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Transactional
    public void sendEmail(Member member) {

        member.generateEmailCheckToken();

        String url = "<html><body>" +
                "<p>링크 : <a href=\"https://localhost:8443/email_check_token?token=" +
                member.getEmailCheckToken() + "&email=" + member.getEmail() +
                "\">이메일 인증을 원하시는 경우 이곳을 클릭하세요.</a></p>" +
                "</body></html>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(member.getEmail());
            mimeMessageHelper.setSubject("ncz 회원 가입 인증");
            mimeMessageHelper.setText(url, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            log.error("이메일 전송 실패; ({})", member.getEmail());
        }
    }
}
