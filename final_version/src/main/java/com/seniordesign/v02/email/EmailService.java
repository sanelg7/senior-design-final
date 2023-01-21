package com.seniordesign.v02.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);



    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Registration for myNotes");
            helper.setFrom("myNotes@registration.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("can not send email",e);
            throw new IllegalStateException("Can not send email");
        }

    }

    public void sendContactUs(String email,String name,String surname,String senderEmail,String subject,String content){
        if(email.isEmpty()){
            email = senderEmail;
        }
        String to = "sanelegegame@gmail.com";
        String sender = name + " " + surname;
        String actualcontent = "From : " + name + " " + surname + "\n" +
                "Sender email : " + email + "\n" +
                "Content : " + content;
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(actualcontent,false);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(senderEmail);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("can not send email",e);
            throw new IllegalStateException("Can not send email");
        }


    }
}
