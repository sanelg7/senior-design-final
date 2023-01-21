package com.seniordesign.v02.password.secure_key;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateSecureKeyRequest {

    private final String email;
    private final String token;
    private final long passwordID;
    private final String oldSecureKey;
    private final String newSecureKey;

}
