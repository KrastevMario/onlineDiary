package com.diary_online.diary_online.controller;


import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.model.dto.SafeUserDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class DiaryController extends AbstractController{

    @Autowired
    DiaryService diaryService;

    @PutMapping("/user/{user_id}/diaries")
    public String addDiary(@PathVariable(name = "user_id") int userId, @RequestBody Diary diary, HttpSession ses){

        return diaryService.addDiary(userId,diary,ses);
    }
}
