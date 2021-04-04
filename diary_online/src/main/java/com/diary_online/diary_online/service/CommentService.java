package com.diary_online.diary_online.service;

import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.CommentRepository;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
import com.diary_online.diary_online.util.UtilSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    SectionDbDAO sectionDbDAO;

    public String addComment( Comment comment,int userId, int sectionId) {

       Optional<Section> sec = sectionRepository.findById(sectionId);
       if(!sec.isPresent()){
           throw new NotFoundException("Cannot found section");
       }
       Section section = sec.get();
        User user = userRepository.findById(userId).get();

        List<SectionFromDbDTO> accessibleSection = new ArrayList<>();
        accessibleSection.addAll(sectionDbDAO.getPublicSectionsFollowedByMe(userId));
        accessibleSection.addAll(sectionDbDAO.getSharedWithMeSection(userId));
        accessibleSection.addAll(sectionDbDAO.getMySection(userId));

        for (SectionFromDbDTO s: accessibleSection) {
            if(s.getId() == sectionId && UtilSection.isVisible(section)){  //i have private sections
                Comment c = new Comment();
                c.setCommentOwner(user);
                c.setCreatedAt(LocalDateTime.now());
                c.setCommentSection(section);
                c.setContent(comment.getContent());

                commentRepository.save(c);

                return "comment added successful.";
            }
        }

        throw new NotFoundException("Cannot found section");
    }

    public String deleteComment(int userId, int commentId) {
        User user = userRepository.findById(userId).get();
        Optional<Comment> c = commentRepository.findById(commentId);
        if(!c.isPresent()){
            throw new NotFoundException("comment not found");
        }
        Comment comment = c.get();

        if(comment.getCommentOwner() == user){
            commentRepository.deleteById(commentId);
            return "you delete comment successful";
        }
        throw new BadRequestException("You are not the owner of the comment");
    }

    public Comment getComment(int userId, int commentId) {
        Optional<Comment> com = commentRepository.findById(commentId);
        if(!com.isPresent()){
            throw new NotFoundException("comment not found");
        }

        Comment comment = com.get();

        if(comment.getCommentOwner().getId() == userId){
            return comment;
        }
        throw new BadRequestException("You are not the ownre of the comment");
    }
}
