package com.seniordesign.v02.note;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NoteRequest {

    private final String email;
    private final long noteID;
    private final long noteCategoryID;
    private final String token;
    private final String noteTitle;
    private final String noteDescription;
    private final String noteContent;


}