package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class SectionController {
    @Autowired
    SectionService sectionService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/users/diaries/{diary_id}/addSection")
    public String addSection(@PathVariable(name = "diary_id") int diaryId,@RequestBody Section section, HttpSession ses){
        //TODO: VERIFY
        int userId = sessionController.getLoggedUser(ses).getId();
        return sectionService.addSection(userId,diaryId,section,ses);
    }


    @PostMapping("/section/{section_id}")
    public String updateSection(@PathVariable(name = "section_id") int sectionId,@RequestBody Section section){
       return sectionService.updateSection(sectionId,section);
    }
}
