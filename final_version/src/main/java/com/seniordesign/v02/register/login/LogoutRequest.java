package com.seniordesign.v02.register.login;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;



@Getter
@EqualsAndHashCode
@ToString
public class LogoutRequest {
    private final String email;
    private final String token;

    public LogoutRequest(String email, String token){
        this.email=email;
        this.token=token;
    }
}
