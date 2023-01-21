package com.seniordesign.v02.email;

public interface EmailSender {
    void send(String to,String email);
    void sendContactUs(String email,String name,String surname,String senderEmail,String subject,String content);
}
