package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.dto.UserFromDbDTO;
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



    @PutMapping("/users/follow/{fuser_id}")
    public String followUser(@PathVariable(name = "fuser_id") int fuserId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            return "You are not logged in. Please log in.";
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.followUser(userId,fuserId);
    }

    @GetMapping("/users/logout")
    public String logout(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            return "You are not logged in yet. Cannot log out.";
        }
        sessionController.logoutUser(session);
        return "Successfully logged out.";
    }

    @GetMapping("/followedUsers/public/section")
    public List<SectionFromDbDTO> getPublicSectionFromFollowedUsers(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.getPublicSectionFromFollowedUsers(userId);
    }

    @GetMapping("/users/sections")
    public List<SectionFromDbDTO> getMySection(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.getMySections(userId);
    }

    @DeleteMapping("/users/unfollow/{userToUnfollow}")
    public String unfollowUser(@PathVariable(name = "userToUnfollow") int userToUnfollow, HttpSession session){
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.unfollowUser(userId,userToUnfollow);
    }

    @PostMapping("/users/edit")
    public String updateUser(@RequestBody User userNewInfo, HttpSession session){
        //TODO: Verify
        if(sessionController.isLoggedIn(session)){
            int myId = sessionController.getLoggedUser(session).getId();
            return userService.updateUser(userNewInfo,myId);
        }
        else{
            return "You are not logged in";
        }
    }

    @GetMapping("/users/followers")
    public List<UserFromDbDTO> myFollowers(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.showMyFollowers(userId);
    }

    @GetMapping("/users/shared/sections")
    public List<SectionFromDbDTO> getSharedSectionsWitMe(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.showSharedSectionsWithMe(userId);
    }

}
