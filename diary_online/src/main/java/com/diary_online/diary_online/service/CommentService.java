package com.diary_online.diary_online.service;

import com.diary_online.diary_online.model.dao.CommentDAO;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dto.SectionFromDbDTO;
import com.diary_online.diary_online.model.pojo.Comment;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.CommentRepository;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
import com.diary_online.diary_online.util.UtilUser;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    public String addComment( Comment comment,int userId, int sectionId) {

       Optional<Section> sec = sectionRepository.findById(sectionId);
       if(!sec.isPresent()){
           return "Cannot found section";
       }
       Section section = sec.get();
        User user = userRepository.findById(userId).get();

        List<SectionFromDbDTO> accessibleSection = new ArrayList<>();
        accessibleSection.addAll(SectionDbDAO.getPublicSectionsFollowedByMe(userId));
        accessibleSection.addAll(SectionDbDAO.getSharedWithMeSection(userId));
        accessibleSection.addAll(SectionDbDAO.getMySection(userId));

        for (SectionFromDbDTO s: accessibleSection) {
            if(s.getId() == sectionId && UtilUser.isVisible(section)){
                Comment c = new Comment();
                c.setCommentOwner(user);
                c.setCreatedAt(LocalDateTime.now());
                c.setCommentSection(section);
                c.setContent(comment.getContent());

                commentRepository.save(c);

                return "comment added successful.";
            }
        }

        return "Cannot found section";
    }

    public String deleteComment(int userId, int sectionId) {
        commentRepository.deleteById(CommentDAO.findCommentByUserAndSectionId(userId,sectionId));
        return "you delete comment successful";
    }
}
