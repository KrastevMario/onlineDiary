package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.User;
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

    @PutMapping("/user/diaries")
    public String addDiary(@RequestBody Diary diary, HttpSession ses){
        int userId = sessionController.getLoggedUser(ses).getId();
        return diaryService.addDiary(userId,diary,ses);
    }

    @GetMapping("/user/diaries/{diary_id}")
    public Diary getDiary(@RequestParam(value = "diary_id") int diaryId){
        return diaryService.getDiary(diaryId);
    }
}
