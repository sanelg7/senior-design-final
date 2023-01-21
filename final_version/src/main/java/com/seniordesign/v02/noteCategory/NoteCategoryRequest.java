package com.seniordesign.v02.noteCategory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NoteCategoryRequest {

    private final String email;
    private final String token;
    private final String noteCategoryTitle;
    private final long noteCategoryID;


}