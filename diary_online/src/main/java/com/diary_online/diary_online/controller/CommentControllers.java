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

    @PostMapping("/users/{user_id}/comment/{section_id}")
    public String comment( @RequestBody Comment comment ,@PathVariable(name = "user_id") int userId, @PathVariable(name = "section_id") int sectionId, HttpSession session){
        return commentService.comment(comment,userId,sectionId,session);
    }
}
