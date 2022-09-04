package com.bridgelabz.lmsadmin.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class AdminNotFoundException extends RuntimeException{
    private int statusCode;
    private String statusMessage;

    public AdminNotFoundException(int statusCode, String statusMessage) {
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
