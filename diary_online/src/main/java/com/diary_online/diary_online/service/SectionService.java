package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
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
}
