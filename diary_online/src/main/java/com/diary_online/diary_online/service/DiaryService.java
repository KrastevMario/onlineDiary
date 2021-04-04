package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.DiaryRepository;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Autowired
    SectionDbDAO sectionDbDAO;

    public String addDiary(int userId, Diary diary){

        User owner = userRepository.findById(userId).get();
        diary.setOwner(owner);
        diary.setCreatedAt(LocalDateTime.now());
        diaryRepository.save(diary);
        return "You successfully added diary " + diary.getTitle();
    }

    public String updateDiary(int userId, int diaryId, Diary diary) {
        Optional<Diary> d = diaryRepository.findById(diaryId);
        if(!d.isPresent()){
            throw new NotFoundException("diary not found");
        }
        User user = userRepository.findById(userId).get();

        for (Diary userDiary:user.getDiaries()) {
            if(userDiary.getId() == diaryId){
                userDiary.setTitle(diary.getTitle());
                diaryRepository.save(userDiary);
                return "updated diary successful";
            }
        }
        throw new NotFoundException("diary not found");
    }

    @Transactional
    public String deleteDiary(int userId, int diaryId) {

        Optional<Diary> checkDiary = diaryRepository.findById(diaryId);
        if(!checkDiary.isPresent()){
            throw new NotFoundException("diary not found");
        }
        User currentUser = userRepository.findById(userId).get();

        for (Diary d : currentUser.getDiaries()) {
            if(d.getId() == diaryId){
                sectionDbDAO.deleteChildSections(diaryId);
                diaryRepository.deleteById(diaryId);
                return "you delete diary with id " + diaryId;
            }
        }
        throw new NotFoundException("diary not found");
    }

    public Diary getDiary(int diaryId) {
        //TODO: FINISH BY RETURNING ALL THE INFO
        if(diaryRepository.findById(diaryId).isEmpty()){
            throw new BadRequestException("Invalid diary.");
        }
        return diaryRepository.findById(diaryId).get();
    }
}
