package com.seniordesign.v02.register;

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
        private final String secretQuestion;
        private final String secretQuestionAnswer;

    }
