package com.diary_online.diary_online.controller;

import com.diary_online.diary_online.exceptions.AuthenticationException;
import com.diary_online.diary_online.model.dto.CommentDTO;
import com.diary_online.diary_online.model.dto.SectionDTO;
import com.diary_online.diary_online.model.dto.SuccessDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
public class CommentController extends AbstractController{

    @Autowired
    CommentService commentService;
    @Autowired
    SessionController sessionController;

    @PutMapping("/comments/sections/{section_id}")
    public CommentDTO addComment( @RequestBody Comment comment, @PathVariable(name = "section_id") int sectionId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new CommentDTO(commentService.addComment(comment,userId,sectionId));
    }

    @DeleteMapping("/comments/{comment_id}")
    public CommentDTO deleteComment( @PathVariable(name = "comment_id") int commentId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new CommentDTO(commentService.deleteComment(userId,commentId));
    }

    @GetMapping("comments/{comment_id}")
    public CommentDTO getComment(@PathVariable(value = "comment_id") int commentId, HttpSession session){
        if(!sessionController.isLoggedIn(session)){
            throw new AuthenticationException("You must be logged in to use this option.");
        }
        int userId = sessionController.getLoggedUser(session).getId();
        return new CommentDTO(commentService.getComment(userId,commentId));
    }
}
