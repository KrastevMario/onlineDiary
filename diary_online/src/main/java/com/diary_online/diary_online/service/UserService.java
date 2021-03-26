package com.diary_online.diary_online.service;

import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String addUser(User user) {
        //verify all data
        if(user == null){
            return "Invalid input";
        }
        //set all data
        User currentUser = new User();
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setUsername(user.getUsername());
        currentUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(currentUser);

        return "Successful registration";
    }
}
