package org.noteam.be.teamMember.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.noteam.be.teamMember.service.SendEmailService;
import org.noteam.be.teamMember.repository.MemberRepository;
import org.noteam.be.teamMember.domain.Member;
import org.noteam.be.teamMember.dto.InviteMemberResponse;
import org.noteam.be.system.exception.EmailSendException;
import org.noteam.be.system.exception.ExceptionMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {


    private final JavaMailSender mailSender;
    private final InviteMemberResponse inviteMemberResponse = new InviteMemberResponse();
    private final MemberRepository memberRepository;

    @Value("${custom.mail}")
    private String sender;

    private String url = "http://localhost:8080/test3";


    @Override
    public InviteMemberResponse sendInviteEmail(String inviter, long teamId, long memberId) {

        MimeMessage message = mailSender.createMimeMessage();

        Member member = memberRepository.findById(memberId).orElseThrow();
        String receiverEmail = member.getEmail();
        log.info("받는사람 이메일 = {}", receiverEmail);

        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(receiverEmail);
            helper.setSubject("[Accord] " + inviter + "님이 팀에 초대를 했습니다");
            helper.setText("<h3>안녕하세요!</h3><p>"+inviter+"님이 초대를 하셨습니다</p>" +
                            "<a href='" + url + "/" + teamId + "/" + memberId + "' " +
                            "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; " +
                            "background-color: #0078D7; text-decoration: none; border-radius: 5px;'>팀에 가입하기</a>",
                    true);
            mailSender.send(message);

            return inviteMemberResponse.builder()
                    .message("Success Send Email")
                    .result(true)
                    .build();

        } catch (MessagingException e) {
            throw new EmailSendException(ExceptionMessage.EMAIL_SENDING_ERROR);
        }


    }
}
