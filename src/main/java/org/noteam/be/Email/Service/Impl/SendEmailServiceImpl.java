package org.noteam.be.Email.Service.Impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.system.exception.ExceptionMessage;
import org.noteam.be.system.exception.email.InvalidEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;

    @Value("${custom.mail}")
    private String sender;

    private String url = "http://localhost:8080/test/";

    private int num1,num2;

    private String inviter = "석환";


    public void sendInviteEmail(String receiver)  {

        MimeMessage message = mailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("[Accord] " + inviter + "님이 팀에 초대를 했습니다");
            helper.setText("<h3>안녕하세요!</h3><p>"+inviter+"님이 초대를 하셨습니다</p>" +
                            "<a href='" + url + "/" + num1 + "/" + num2 + "' " +
                            "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; " +
                            "background-color: #0078D7; text-decoration: none; border-radius: 5px;'>Go to Naver</a>",
                    true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new InvalidEmailException(ExceptionMessage.EMPTY_EMAIL_ERROR);
        }

        log.info("mailSender = {}", mailSender);

    }

}
