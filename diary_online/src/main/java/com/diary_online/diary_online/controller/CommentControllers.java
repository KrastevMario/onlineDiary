package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.SuccessDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
public class CommentControllers extends AbstractController{

    @Autowired
    CommentService commentService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/comments/sections/{section_id}")
    public SuccessDTO addComment( @RequestBody Comment comment, @PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(commentService.addComment(comment,userId,sectionId));
    }

    @DeleteMapping("/comments/{coment_id}")
    public SuccessDTO deleteComment( @PathVariable(name = "coment_id") int comentId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new SuccessDTO(commentService.deleteComment(userId,comentId));
    }
}
