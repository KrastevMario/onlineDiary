package com.diary_online.diary_online.service;

import com.diary_online.diary_online.model.dao.CommentDAO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.CommentRepository;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SectionRepository sectionRepository;

    public String addComment( Comment comment ,int userId, int sectionId) {

        Optional<User> user = userRepository.findById(userId);

        User u = user.get();
        Section s = sectionRepository.findById(sectionId).get();

        Comment c = new Comment();
        c.setCommentOwner(u);
        c.setCreatedAt(LocalDateTime.now());
        c.setCommentSection(s);
        c.setContent(comment.getContent());

        commentRepository.save(c);

        return "comment added successful.";
    }

    public String deleteComment(int userId, int sectionId) {
        commentRepository.deleteById(CommentDAO.findCommentByUserAndSectionId(userId,sectionId));
        return "you delete comment successful";
    }
}
