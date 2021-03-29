package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class SectionController {
    @Autowired
    SectionService sectionService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/user/diary/{diary_id}/addSection")
    public String addSection(@PathVariable(name = "diary_id") int diaryId,@RequestBody Section section, HttpSession ses){
        int userId = sessionController.getLoggedUser(ses).getId();
        return sectionService.addSection(userId,diaryId,section,ses);
    }}
