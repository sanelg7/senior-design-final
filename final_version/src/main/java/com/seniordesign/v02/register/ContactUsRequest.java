package com.seniordesign.v02.register;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ContactUsRequest {

    private final String email;
    private final String name;
    private final String surname;
    private final String senderEmail;
    private final String subject;
    private final String content;

}