package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.DiaryRepository;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DiaryService {
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserRepository userRepository;

    public String addDiary(int userId, Diary diary, HttpSession ses){

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }

        SafeUserDTO loggedUser = sessionController.getLoggedUser(ses);
        if(loggedUser.getId() != userId){
            throw new BadRequestException("You cannot make diary from another user profile, please login in yours");
        }

        User owner = user.get();
        diary.setOwner(owner);
        diary.setCreatedAt(LocalDateTime.now());
        diaryRepository.save(diary);
        return "You successfully added the diary " + diary.getTitle();
    }

    public Diary getDiary(int diaryId) {
        //TODO: VERIFY if diary exists
        return diaryRepository.findById(diaryId).get();
    }


    public String updateDiary(int diaryId, Diary diary) {
        Diary d = diaryRepository.findById(diaryId).get();
        d.setTitle(diary.getTitle());
        diaryRepository.save(d);

        return "updated diary successful";
    }

    @Transactional
    public String deleteDiary(int userId, int diaryId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }

        User currentUser = user.get();

        for (Diary d : currentUser.getDiaries()) {
            if(d.getId() == diaryId){
                SectionDbDAO.deleteChildSections(diaryId);
                diaryRepository.deleteById(diaryId);
                return "you delete diary with id " + diaryId;
            }
        }
        return "Can't find the chosen diary.";
    }
}
