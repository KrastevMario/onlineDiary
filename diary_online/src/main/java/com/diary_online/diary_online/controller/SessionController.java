package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionController extends AbstractController{

    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";

    @Autowired
    private UserRepository repository;

    public SafeUserDTO getLoggedUser(HttpSession session){
        if(session.getAttribute(LOGGED_USER_ID) == null){
            throw new AuthenticationException("You are not logged in! Please, log in.");
        }
        else{
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            return new SafeUserDTO(repository.findById(userId).get());
        }
    }

    public void loginUser(HttpSession ses, int id) {
        if(repository.findById(id).isPresent()) {
            ses.setAttribute(LOGGED_USER_ID, id);
            return;
        }
        throw new BadRequestException("Invalid user id");
    }

    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }

    public boolean isLoggedIn(HttpSession session){
        return session.getAttribute("LOGGED_USER_ID") != null;
    }
}
