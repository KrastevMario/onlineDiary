package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.service.SectionService;
import com.diary_online.diary_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class SectionController {
    @Autowired
    SectionService sectionService;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserService userService;

    @PutMapping("/users/diaries/{diary_id}/addSection")
    public String addSection(@PathVariable(name = "diary_id") int diaryId,@RequestBody Section section, HttpSession ses){
        //TODO: VERIFY
        int userId = sessionController.getLoggedUser(ses).getId();
        return sectionService.addSection(userId,diaryId,section,ses);
    }

    @PutMapping("/users/likes/{section_id}")
    public String likeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        //TODO: VERIFICATION
        if(!sessionController.isLoggedIn(session)){
            return "You are not logged in. You must login to be able to like a section.";
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.likeSection(userId,sectionId);
    }

    @PostMapping("/sections/{section_id}")
    public String updateSection(@PathVariable(name = "section_id") int sectionId,@RequestBody Section section){
       return sectionService.updateSection(sectionId,section);
    }

    @PutMapping("/users/dislikes/{section_id}")
    public String dislikeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        //TODO: VERIFICATION
        if(sessionController.isLoggedIn(session)){
            return "You are not logged in. Please log in.";
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return userService.dislikeSection(userId,sectionId,session);
    }

    @PutMapping("/shares/{section_id}/users/{user_id}")
    public String shareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        //TODO: Verification
        if(sessionController.isLoggedIn(session)){
            return userService.shareSection(userId,sectionId);
        }
        else{
            return "You are not logged in. Please log in.";
        }
    }

    @DeleteMapping("/unshares/{section_id}/users/{user_id}")
    public String unshareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        //TODO: Verification
        if(sessionController.isLoggedIn(session)){
            return userService.unshareSection(userId,sectionId);
        }
        else{
            return "You are not logged in";
        }
    }
}
