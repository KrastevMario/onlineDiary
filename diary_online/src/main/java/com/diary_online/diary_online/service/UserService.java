package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.CommentRepository;
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

    public List<SafeUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<SafeUserDTO> safeUsers = new ArrayList<>();

        for (User u: users) {
            safeUsers.add(new SafeUserDTO(u));
        }

        return safeUsers;
    }

    public String likeSection(int userId,int sectionId, HttpSession session) {

        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getLikers().add(u);
        sectionRepository.save(s);
        return "You liked section with id : " + sectionId;
    }

    public String dislikeSection(int userId, int sectionId, HttpSession session) {
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getDisLikers().add(u);
        sectionRepository.save(s);
        return "You disliked section with id : " + sectionId;
    }

    public String shareSection(int userId, int sectionId, HttpSession session) {
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getUsersSharedWith().add(u);
        sectionRepository.save(s);
        return "You shared section with id : " + sectionId + " with user with id " + userId;
    }

    public String followUser(int userId, int fuserId, HttpSession session) {
        User u = userRepository.findById(userId).get();
        User fu = userRepository.findById(fuserId).get();

        fu.getFollowers().add(u);
        userRepository.save(fu);

        return "You follow another user";
    }


}
