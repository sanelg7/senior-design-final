package com.seniordesign.v02.password;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PasswordRequest {

    private final String email;
    private final String token;
    private final long passwordCategoryID;
    private final long passwordID;
    private final String passwordTitle;
    private final String passwordDescription;
    private final boolean secure;
    private final String secureKey;
    private final String passwordContent;
    private final String userPassword;
    private final String secretQuestion;
    private final String secretQuestionAnswer;


}