package com.diary_online.diary_online.controller;

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

    @PutMapping("/users/comment/{section_id}")
    public String comment( @RequestBody Comment comment, @PathVariable(name = "section_id") int sectionId, HttpSession session){
        int userId = sessionController.getLoggedUser(session).getId();
        return commentService.comment(comment,userId,sectionId,session);
    }
}
