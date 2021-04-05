package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.*;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends AbstractController{

    @Autowired
    UserService userService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/users")
    public UserDTO addUser(@RequestBody User user, HttpSession session){
        if(sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You are already logged in");
        }
        return new UserDTO(userService.addUser(user));
    }

    @PostMapping("/users")
    public SafeUserDTO loginUser(@RequestBody LoginUserDTO loginCredentials, HttpSession session){
        //check if user is already logged in
        if(sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You are already logged in");
        }
        //login user
        User user = userService.login(loginCredentials);
        sessionController.loginUser(session, user.getId());
        return new SafeUserDTO(user);

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



    @PutMapping("/users/follow/{userToFollow_id}")
    public SuccessDTO followUser(@PathVariable(name = "userToFollow_id") int userToFollow, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You are not logged in. Please log in.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(userService.followUser(userId,userToFollow));
    }

    @GetMapping("/users/logout")
    public SuccessDTO logout(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You are not logged in yet. Cannot log out.");
        }
        sessionController.logoutUser(session);
        return new SuccessDTO("Successfully logged out.");
    }

    @GetMapping("/followedUsers/public/section")
    public List<SectionDTO> getPublicSectionFromFollowedUsers(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        List<SectionDTO> sectionsAfterCasting = new ArrayList<>(); //parsing the sections into their DTO
        List<Section> sections = new ArrayList<>(userService.getPublicSectionFromFollowedUsers(userId)); //The crude sections
        for (Section section : sections){
            sectionsAfterCasting.add(new SectionDTO(section));
        }
        return sectionsAfterCasting;
    }

    @DeleteMapping("/users/unfollow/{userToUnfollow}")
    public SuccessDTO unfollowUser(@PathVariable(name = "userToUnfollow") int userToUnfollow, HttpSession session){
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(userService.unfollowUser(userId,userToUnfollow));
    }

    @PostMapping("/users/edit")
    public SuccessDTO updateUser(@RequestBody User userNewInfo, HttpSession session){
        if(sessionController.isLoggedIn(session)){
            int myId = sessionController.getLoggedUser(session).getId();
            return new SuccessDTO(userService.updateUser(userNewInfo,myId));
        }
        else{
            return new SuccessDTO("You are not logged in");
        }
    }

    @GetMapping("/users/followers")
    public List<UserFromDbDTO> myFollowers(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
//        List<User> fullUsers = userService.showMyFollowers(userId);
//        List<UserFromDbDTO> usersDTO = new ArrayList<>();
//        for (User fullUser : fullUsers){
//            usersDTO.add(new UserFromDbDTO(fullUser));
//        }
        //return usersDTO;
        return userService.showMyFollowers(userId)
                .stream()
                .map(userThis -> new UserFromDbDTO(userThis))
                .collect(Collectors.toList());
    }

    @GetMapping("/users/shared/sections")
    public List<SectionDTO> getSharedSectionsWitMe(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.showSharedSectionsWithMe(userId)
                .stream()
                .map(section -> new SectionDTO(section))
                .collect(Collectors.toList());
    }
}
