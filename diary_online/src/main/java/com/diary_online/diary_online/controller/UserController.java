package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    @Autowired
    UserService userService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/users")
    public String addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/users")
    public String loginUser(@RequestBody LoginUserDTO loginCredentials, HttpSession session){
        User user = userService.login(loginCredentials);
        sessionController.loginUser(session, user.getId());
        return "Login Successful!";
    }

    @GetMapping("/users/{id}")
    public SafeUserDTO getUser(@PathVariable int id){
        return userService.getUser(id);
    }

    @GetMapping("/users/current")
    public SafeUserDTO getCurrentSessionUser(HttpSession session){
        return userService.getCurrentSessionUser(session);
    }

    @GetMapping("/users")
    public List<SafeUserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/users/{user_id}/like/{section_id}")
    public String likeSection(@PathVariable(name = "user_id") int userId,@PathVariable(name = "section_id") int sectionId, HttpSession session){
        return userService.likeSection(userId,sectionId,session);
    }


    @PutMapping("/users/{user_id}/dislike/{section_id}")
    public String dislikeSection(@PathVariable(name = "user_id") int userId,@PathVariable(name = "section_id") int sectionId, HttpSession session){
        return userService.dislikeSection(userId,sectionId,session);
    }

    @PutMapping("/users/{user_id}/share/{section_id}")
    public String shareSection(@PathVariable(name = "user_id") int userId,@PathVariable(name = "section_id") int sectionId, HttpSession session){
        return userService.shareSection(userId,sectionId,session);
    }

    @PutMapping("/users/{user_id}/follow/{fuser_id}")
    public String followUser(@PathVariable(name = "user_id") int userId,@PathVariable(name = "fuser_id") int fuserId, HttpSession session){
        return userService.followUser(userId,fuserId,session);
    }
}
