package com.seniordesign.v02.register.login;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class LoginRequest {

    private final String email;
    private final String password;


    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
