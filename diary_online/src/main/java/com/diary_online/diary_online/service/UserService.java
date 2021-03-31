package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.*;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dao.UserDAO;
import com.diary_online.diary_online.model.dto.LoginUserDTO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.dto.UserFromDbDTO;
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


    public String addUser(User user) {
        //verify all data
        if (user == null) {
            return "Invalid info!";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("Email already exists!");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BadRequestException("Username already exists!");
        }
        //verify the integrity of "password"
        String userPassword = user.getPassword();


        if (!userPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
            throw new NotSecuredEnoughInputException("The password must have at least 6 characters, 1 numeric character," +
                    " 1 lowercase alphabetical character, 1 uppercase alphabetical character");
        }


        //hash password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwdHashed = encoder.encode(userPassword);
        //set all data
        user.setPassword(pwdHashed);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "Successful registration";
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
            throw new UserDoesNotExistException("The user does not exist.");
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

    public String likeSection(int userId, int sectionId) {

        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getLikers().add(u);
        sectionRepository.save(s);
        return "You liked section with id : " + sectionId;
    }

    public String dislikeSection(int userId, int sectionId) {
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getDisLikers().add(u);
        sectionRepository.save(s);
        return "You disliked section with id : " + sectionId;
    }

    public String shareSection(int userId, int sectionId) {
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getUsersSharedWith().add(u);
        sectionRepository.save(s);
        return "You shared section with id : " + sectionId + " with user with id " + userId;
    }


    public String unshareSection(int userId, int sectionId) {
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getUsersSharedWith().remove(u);
        sectionRepository.save(s);
        return "You unshared section with id : " + sectionId + " with user with id " + userId;
    }

    public String followUser(int userId, int fuserId, HttpSession session) {
        User u = userRepository.findById(userId).get();
        User fu = userRepository.findById(fuserId).get();

        fu.getFollowers().add(u);
        userRepository.save(fu);

        return "You started following " + fu.getUsername();
    }

    public List<SectionFromDbDTO> getPublicSectionFromFollowedUsers(int userId) {
        return SectionDbDAO.getPublicSectionsFollowedByMe(userId);
    }

    public List<SectionFromDbDTO> getMySections(int userId) {
        return UserDAO.showAllMySection(userId);
    }

    public String unfollowUser(int userId, int fuserId) {
        User u = userRepository.findById(userId).get();
        User fu = userRepository.findById(fuserId).get();

        fu.getFollowers().remove(u);
        userRepository.save(fu);

        return "You unfollow " + fu.getUsername();
    }

    public String updateUser(User user, int myId) {

        //verify all data
        if (user == null) {
            return "Invalid info!";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException("Email already exists!");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BadRequestException("Username already exists!");
        }


        User me = userRepository.findById(myId).get();
        me.setFirstName(user.getFirstName());
        me.setLastName(user.getLastName());
        me.setEmail(user.getEmail());
        me.setUsername(user.getUsername());

        userRepository.save(me);

        return "Successful updating";
    }

    public List<UserFromDbDTO> showMyFollowers(int userId) {
        return UserDAO.showFollowers(userId);
    }

    public List<SectionFromDbDTO> showSharedSectionsWithMe(int userId) {
        return SectionDbDAO.getSharedWithMeSection(userId);
    }
}