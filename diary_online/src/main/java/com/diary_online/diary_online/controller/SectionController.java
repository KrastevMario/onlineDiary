package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.SuccessDTO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.service.SectionService;
import com.diary_online.diary_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class SectionController extends AbstractController{
    @Autowired
    SectionService sectionService;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserService userService;

    @PutMapping("/sections/diaries/{diary_id}")
    public SuccessDTO addSection(@PathVariable(name = "diary_id") int diaryId, @RequestBody Section section, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.addSection(userId,diaryId,section));
    }

    @DeleteMapping("/sections/{section_id}")
    public SuccessDTO deleteSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.deleteSection(userId,sectionId));
    }

    @PostMapping("/sections/{section_id}")
    public SuccessDTO updateSection(@PathVariable(name = "section_id") int sectionId,@RequestBody Section section,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.updateSection(sectionId,section,userId));
    }

    @PutMapping("/users/likes/{section_id}")
    public SuccessDTO likeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.likeSection(userId,sectionId));
    }


    @PutMapping("/users/dislikes/{section_id}")
    public SuccessDTO disLikeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.dislikeSection(userId,sectionId));
    }

    @PutMapping("/sections/{section_id}/users/{user_id}")
    public SuccessDTO shareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int myId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.shareSection(myId,userId,sectionId));
    }

    @DeleteMapping("/sections/{section_id}/users/{user_id}")
    public SuccessDTO unshareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int myId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.unshareSection(myId,userId,sectionId));
    }

    @DeleteMapping("likes/sections/{section_id}")
    public SuccessDTO deleteLike(@PathVariable(name = "section_id") int sectionId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.removeLike(userId,sectionId));
    }

    @DeleteMapping("dislikes/sections/{section_id}")
    public SuccessDTO deleteDislike(@PathVariable(name = "section_id") int sectionId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(sectionService.removeDislike(userId,sectionId));
    }
}
