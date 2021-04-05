package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.*;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dao.UserDAO;
import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.dto.SectionDTO;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LoginUserDTO loginUserDTO;
    @Autowired
    SessionController sessionController;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    SectionDbDAO sectionDbDAO;
    @Autowired
    UserDAO userDAO;


    public User addUser(User user) {
        //verify all data
        //check if all the info is different than Null
        if (user == null) {
            throw new BadRequestException("Invalid info!");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Please insert an email. Registration failed.");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("Please insert a username. Registration failed.");
        }
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new BadRequestException("Please insert your first name. Registration failed.");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new BadRequestException("Please insert your last name. Registration failed.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BadRequestException("Please insert a password. Registration failed.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }
        //verify the integrity of "password"
        String userPassword = user.getPassword();
        if (!userPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
            throw new BadRequestException("The password must have at least 6 characters, 1 numeric character," +
                    " 1 lowercase alphabetical character, 1 uppercase alphabetical character");
        }
        //hash password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwdHashed = encoder.encode(userPassword);
        //set all data
        user.setPassword(pwdHashed);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    public User login(LoginUserDTO loginCredentials) {
        User user = userRepository.findByUsername(loginCredentials.getUsername());
        if (user == null) {
            throw new AuthenticationException("Invalid Credentials!");
        } else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(loginCredentials.getPassword(), user.getPassword())) {
                return user;
            } else {
                throw new AuthenticationException("Invalid Credentials!");
            }
        }
    }

    public SafeUserDTO getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("The user does not exist.");
        }
        return new SafeUserDTO(user.get());
    }

    public SafeUserDTO getCurrentSessionUser(HttpSession session) {
        return sessionController.getLoggedUser(session);
    }

    public List<SafeUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<SafeUserDTO> safeUsers = new ArrayList<>();

        for (User u : users) {
            safeUsers.add(new SafeUserDTO(u));
        }

        return safeUsers;
    }

    public User followUser(int userId, int userToFollowId) {
        //not allow user to follow himself
        if(userId == userToFollowId){
            throw new BadRequestException("You cannot follow yourself.");
        }
        //check if the user exists
        if (userRepository.findById(userId).isEmpty()) {
            throw new BadRequestException("Something went wrong while trying to execute your request.");
        }
        //check if the follower exists
        if (userRepository.findById(userToFollowId).isEmpty()) {
            throw new BadRequestException("You are trying to follow an invalid user.");
        }
        //check if the user is already following the followedUser
        if (userDAO.isAlreadyFollowing(userId, userToFollowId)) {
            throw new BadRequestException("You are already following this user. Cannot follow twice the same user.");
        }

        User user = userRepository.findById(userId).get();
        User userToFollow = userRepository.findById(userToFollowId).get();
        userToFollow.getFollowers().add(user);
        userRepository.save(userToFollow);

        return userToFollow;
    }

    public User unfollowUser(int userId, int userToUnfollowId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new AuthenticationException("There's a problem in reading your authentication. Please try to relog to fix the issue.");
        }
        if(userRepository.findById(userToUnfollowId).isEmpty()){
            throw new BadRequestException("The user you are trying to unfollow is invalid.");
        }

        User user = userRepository.findById(userId).get();
        User unfollowUser = userRepository.findById(userToUnfollowId).get();
        //check if the user is following the other user (userToUnfollow)
        if(!userDAO.isAlreadyFollowing(userId, userToUnfollowId)){
            throw new BadRequestException("Cannot unfollow the user. You are not following " + unfollowUser.getUsername());
        }
        unfollowUser.getFollowers().remove(user);
        userRepository.save(unfollowUser);

        return unfollowUser;
    }

    public List<Section> getPublicSectionFromFollowedUsers(int userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new BadRequestException("There was a problem reading the user's Id.");
        }
        return sectionDbDAO.getPublicSectionsFollowedByUser(userId);
    }

    public User updateUser(User userNewInfo, int myId) {
        if (userNewInfo == null) { //the ifs can be merged but it is easier to understand like this
            throw new BadRequestException("The user's new info is invalid.");
        }
        //check every info that the user has (that can be modified)
        boolean isUsernameValid = !(userNewInfo.getUsername() == null || userNewInfo.getUsername().isBlank());
        boolean isFirstNameValid = !(userNewInfo.getFirstName() == null || userNewInfo.getFirstName().isBlank());
        boolean isLastNameValid = !(userNewInfo.getLastName() == null || userNewInfo.getLastName().isBlank());
        boolean isEmailValid = !(userNewInfo.getEmail() == null || userNewInfo.getEmail().isBlank());
        if (!isUsernameValid && !isFirstNameValid && !isLastNameValid && !isEmailValid){
            throw new BadRequestException("The data cannot be blank.");
        }

        //set the data after a verification of the input
        User meUser = userRepository.findById(myId).get(); //contains the new info

        if(isEmailValid) {
            if (userRepository.existsByEmail(userNewInfo.getEmail())) {
                throw new BadRequestException("Email already exists!");
            }
            meUser.setEmail(userNewInfo.getEmail());
        }
        if(isFirstNameValid){
            meUser.setFirstName(userNewInfo.getFirstName());
        }
        if(isLastNameValid){
            meUser.setLastName(userNewInfo.getLastName());
        }
        if(isUsernameValid) {
            if (userRepository.existsByUsername(userNewInfo.getUsername())) {
                throw new BadRequestException("Username already exists!");
            }
            meUser.setUsername(userNewInfo.getUsername());
        }
        //save the new info
        userRepository.save(meUser);
        return userRepository.findById(meUser.getId()).get();
    }

    public List<User> showUserFollowers(int userId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new BadRequestException("The user is invalid.");
        }
        return userDAO.showFollowers(userId);
    }
}