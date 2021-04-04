package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.DiaryWithOutOwnerDTO;
import com.diary_online.diary_online.model.dto.SuccessDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class DiaryController extends AbstractController{

    @Autowired
    DiaryService diaryService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/diaries")
    public SuccessDTO addDiary(@RequestBody Diary diary, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(diaryService.addDiary(userId,diary));
    }

    @PostMapping("/diaries/{diary_id}")
    public SuccessDTO updateDiary(@PathVariable(value = "diary_id") int diaryId, @RequestBody Diary diary,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(diaryService.updateDiary(userId,diaryId,diary));
    }

    @DeleteMapping("/diaries/{diary_id}")
    public SuccessDTO deleteDiary(@PathVariable(value = "diary_id") int diaryId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(diaryService.deleteDiary(userId,diaryId));
    }

    @GetMapping("diaries/{diary_id}")
    public DiaryWithOutOwnerDTO getDiary(@PathVariable(value = "diary_id") int diaryId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new DiaryWithOutOwnerDTO(diaryService.getDiary(userId,diaryId));
    }
}
