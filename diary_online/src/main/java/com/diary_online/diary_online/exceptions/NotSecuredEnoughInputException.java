package com.diary_online.diary_online.exceptions;

public class NotSecuredEnoughInputException extends RuntimeException {
    public NotSecuredEnoughInputException(String msg){
        super(msg);
    }
}
