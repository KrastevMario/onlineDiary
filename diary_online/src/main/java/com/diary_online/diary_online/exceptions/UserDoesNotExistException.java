package com.diary_online.diary_online.exceptions;

public class UserDoesNotExistException extends BadRequestException{
    public UserDoesNotExistException(String msg) {
        super(msg);
    }
}
