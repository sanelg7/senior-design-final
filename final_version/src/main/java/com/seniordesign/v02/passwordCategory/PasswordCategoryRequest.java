package com.seniordesign.v02.passwordCategory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;



@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PasswordCategoryRequest {

    private final String email;
    private final String token;
    private final String passwordCategoryTitle;
    private final long passwordCategoryID;


}