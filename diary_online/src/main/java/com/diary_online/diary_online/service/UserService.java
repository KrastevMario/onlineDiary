package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LoginUserDTO loginUserDTO;
    @Autowired
    SessionController sessionController;

    public String addUser(User user) {
        //verify all data
        if(user == null){
            return "Invalid info!";
        }
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new BadRequestException("Email already exists!");
        }
        if(userRepository.findByUsername(user.getUsername()) != null){
            throw new BadRequestException("Username already exists!");
        }

        //hash password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwdHashed = encoder.encode(user.getPassword());
        //set all data
        user.setPassword(pwdHashed);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "Successful registration";
    }

    public User login(LoginUserDTO loginCredentials) {
        User user = userRepository.findByUsername(loginCredentials.getUsername());
        if(user == null){
            throw new AuthenticationException("Invalid Credentials!");
        }
        else{
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(loginCredentials.getPassword(), user.getPassword())){
                return user;
            }
            else{
                throw new AuthenticationException("Invalid Credentials!");
            }
        }
    }

    public SafeUserDTO getUser(int id) {
        User user = userRepository.findById(id).get();
        return new SafeUserDTO(user);
    }

    public SafeUserDTO getCurrentSessionUser(HttpSession session) {
        return sessionController.getLoggedUser(session);
    }
}
