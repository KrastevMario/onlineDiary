package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.ReactedSectionDTO;
import com.diary_online.diary_online.model.dto.ReactionCountDTO;
import com.diary_online.diary_online.model.dto.SectionDTO;
import com.diary_online.diary_online.model.dto.SuccessDTO;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.service.SectionService;
import com.diary_online.diary_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SectionController extends AbstractController{
    @Autowired
    SectionService sectionService;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserService userService;

    @PutMapping("/sections/diaries/{diary_id}")
    public SectionDTO addSection(@PathVariable(name = "diary_id") int diaryId, @RequestBody Section section, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SectionDTO(sectionService.addSection(userId,diaryId,section));
    }

    @DeleteMapping("/sections/{section_id}")
    public SectionDTO deleteSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SectionDTO(sectionService.deleteSection(userId,sectionId));
    }

    @PostMapping("/sections/{section_id}")
    public SectionDTO updateSection(@PathVariable(name = "section_id") int sectionId,@RequestBody Section section,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SectionDTO(sectionService.updateSection(sectionId,section,userId));
    }

    @PutMapping("/users/likes/{section_id}")
    public ReactionCountDTO likeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new ReactionCountDTO(sectionService.likeSection(userId,sectionId));
    }


    @PutMapping("/users/dislikes/{section_id}")
    public ReactionCountDTO disLikeSection(@PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new ReactionCountDTO(sectionService.dislikeSection(userId,sectionId));
    }

    @PutMapping("/sections/{section_id}/users/{user_id}")
    public SectionDTO shareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int myId = sessionController.getLoggedUser(session).getId();
        return new SectionDTO(sectionService.shareSection(myId,userId,sectionId));
    }

    @DeleteMapping("/sections/{section_id}/users/{user_id}")
    public SectionDTO unshareSection(@PathVariable(name = "section_id") int sectionId, @PathVariable(name = "user_id") int userId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int myId = sessionController.getLoggedUser(session).getId();
        return new SectionDTO(sectionService.unshareSection(myId,userId,sectionId));
    }

    @DeleteMapping("likes/sections/{section_id}")
    public ReactionCountDTO deleteLike(@PathVariable(name = "section_id") int sectionId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new ReactionCountDTO(sectionService.removeLike(userId,sectionId));
    }

    @DeleteMapping("dislikes/sections/{section_id}")
    public ReactionCountDTO deleteDislike(@PathVariable(name = "section_id") int sectionId,HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new ReactionCountDTO(sectionService.removeDislike(userId,sectionId));
    }

    @GetMapping("/users/sections")
    public List<ReactedSectionDTO> getUserSections(HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return sectionService.getMySections(userId);
    }

    @GetMapping("sections/{section_id}")
    public ReactedSectionDTO getSection(@PathVariable(value = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return sectionService.getSection(userId,sectionId);
    }
}
