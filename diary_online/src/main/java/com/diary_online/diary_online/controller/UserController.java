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
    public String addUser(@RequestBody User user, HttpSession session){
        if(sessionController.isLoggedIn(session)){
            return "You are already logged in";
        }
        return userService.addUser(user);
    }

    @PostMapping("/users")
    public String loginUser(@RequestBody LoginUserDTO loginCredentials, HttpSession session){
        //check if user is already logged in
        if(sessionController.isLoggedIn(session)){
            return "You are already logged in";
        }
        //login user
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

    @PutMapping("/users/like/{section_id}")
    public String likeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        //TODO: VERIFICATION
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.likeSection(userId,sectionId,session);
    }


    @PutMapping("/users/dislike/{section_id}")
    public String dislikeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        //TODO: VERIFICATION
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.dislikeSection(userId,sectionId,session);
    }

    @PutMapping("/users/share/{section_id}")
    public String shareSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        //TODO: Verification
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.shareSection(userId,sectionId,session);
    }

    @PutMapping("/users/follow/{fuser_id}")
    public String followUser(@PathVariable(name = "fuser_id") int fuserId, HttpSession session){
        //TODO: Verify
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.followUser(userId,fuserId,session);
    }

//    @GetMapping("/users/follow")
//    public List<SafeUserDTO> getMyFollowers(HttpSession session){
//        SafeUserDTO userId = sessionController.getLoggedUser(session);
//        return userService.followers(userId.getId());
//    }
}
