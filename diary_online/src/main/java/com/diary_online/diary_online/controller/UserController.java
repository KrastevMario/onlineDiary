package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.service.UserService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/users")
    public String addUser(@RequestBody User user){
        return userService.addUser(user);
    }
}
