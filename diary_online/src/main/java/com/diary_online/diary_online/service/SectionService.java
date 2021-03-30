package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dao.UserDAO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
import com.diary_online.diary_online.util.UtilUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SectionService {
    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionController sessionController;

    public String addSection(int userId, int diaryId, Section section, HttpSession ses) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }

        SafeUserDTO loggedUser = sessionController.getLoggedUser(ses);
        if(loggedUser.getId() != userId){
            throw new BadRequestException("You cannot make diary from another user profile, please login in yours");
        }

        User currentUser = user.get();

        for (Diary d : currentUser.getDiaries()) {
            if(d.getId() == diaryId){
                section.setCreatedAt(LocalDateTime.now());
                section.setDiary(d);
                sectionRepository.save(section);
                return "Section added successful in " + section.getDiary().getTitle();
            }
        }
        return "Can't find the chosen diary.";
    }

    public String updateSection(int sectionId, Section section) {
        Section sec = sectionRepository.findById(sectionId).get();

        sec.setPrivacy(section.getPrivacy());


        sectionRepository.save(sec);
        return "section with id " + sectionId + " is updated successful";
    }

    public String likeSection(int userId, int sectionId) {
        //check if the section and user exists
        if(sectionRepository.findById(sectionId).isEmpty()){
            return "The section you are trying to like is invalid.";
        }
        if(userRepository.findById(userId).isEmpty()){
            return "The user cannot like this section.";
        }

        Section section = sectionRepository.findById(sectionId).get();
        User user = userRepository.findById(userId).get();
        //check if the section is public. If it is NOT, verify if the user can like it.
        if(!UtilUser.isVisible(section)){
            if(UserDAO.isSectionSharedUser(section, user)){
                if(UserDAO.hasUserLikedSection(user, section)){
                    return "You already liked this section (" + section.getTitle() + "). Cannot double like.";
                }
            }else{
                throw new AuthenticationException("You don't have the permission to like this section.");
            }
        }
        section.getLikers().add(user);
        sectionRepository.save(section);
        return "You liked the section with title \"" + section.getTitle() + "\"";
    }

    public String dislikeSection(int userId, int sectionId, HttpSession session) {
        //TODO: verify
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getDisLikers().add(u);
        sectionRepository.save(s);
        return "You disliked section with id : " + sectionId;
    }

    public String shareSection(int userId, int sectionId) {
        //TODO: verify
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getUsersSharedWith().add(u);
        sectionRepository.save(s);
        return "You shared section with id : " + sectionId + " with user with id " + userId;
    }

    public String unshareSection(int userId, int sectionId) {
        //TODO: verify
        Section s = sectionRepository.findById(sectionId).get();
        User u = userRepository.findById(userId).get();
        s.getUsersSharedWith().remove(u);
        sectionRepository.save(s);
        return "You unshared section with id : " + sectionId + " with user with id " + userId;
    }
}
