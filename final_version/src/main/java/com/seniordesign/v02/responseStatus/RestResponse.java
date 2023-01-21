package com.seniordesign.v02.responseStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestResponse<T> {

    private String status;
    private String message;
    private T data;
}
