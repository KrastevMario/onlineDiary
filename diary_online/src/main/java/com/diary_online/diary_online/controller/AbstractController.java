package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.exceptions.NotSecuredEnoughInputException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AbstractController {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleNotAuthorized(AuthenticationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(BadRequestException e){
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(NotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler(NotSecuredEnoughInputException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // - note that HTTP 422 has not made it into HTTP 1.1 [Use 400 instead]
    public String handleNotSecuredEnoughInputException(NotSecuredEnoughInputException e){
        return e.getMessage();
    }
}
