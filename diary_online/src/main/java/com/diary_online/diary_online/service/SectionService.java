package com.diary_online.diary_online.service;

import com.diary_online.diary_online.controller.SessionController;
import com.diary_online.diary_online.exceptions.BadRequestException;
import com.diary_online.diary_online.exceptions.NotFoundException;
import com.diary_online.diary_online.model.dao.SectionDbDAO;
import com.diary_online.diary_online.model.dao.UserDAO;
import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.Section;
import com.diary_online.diary_online.model.pojo.User;
import com.diary_online.diary_online.repository.SectionRepository;
import com.diary_online.diary_online.repository.UserRepository;
import com.diary_online.diary_online.util.UtilSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SectionDbDAO sectionDbDAO;

    public String addSection(int userId, int diaryId, Section section) {

        User currentUser = userRepository.findById(userId).get();

        for (Diary d : currentUser.getDiaries()) {
            if(d.getId() == diaryId){
                section.setCreatedAt(LocalDateTime.now());
                section.setDiary(d);
                sectionRepository.save(section);
                return "Section added successful in " + section.getDiary().getTitle();
            }
        }
        throw new NotFoundException("Can't find the chosen diary.");
    }

    public String deleteSection(int userId, int sectionId) {
        User currentUser = userRepository.findById(userId).get();

        for (Diary d : currentUser.getDiaries()) {
            for (Section s: d.getSections()) {
                if(s.getId() == sectionId){
                    sectionRepository.deleteById(sectionId);
                    return "Section remove successful";
                }
            }
        }
        throw new NotFoundException("Cannot find the section " + sectionId + " in your diaries");
    }

    public String updateSection(int sectionId, Section section,int userId) {
        User currentUser = userRepository.findById(userId).get();

        for (Diary d : currentUser.getDiaries()) {
            for (Section s: d.getSections()) {
                if(s.getId() == sectionId){
                    Section sec = sectionRepository.findById(sectionId).get();
                    sec.setTitle(section.getTitle());
                    sec.setContent(section.getContent());
                    sec.setPrivacy(section.getPrivacy());
                    sectionRepository.save(sec);

                    return "Section update successful";
                }
            }
        }
        throw new NotFoundException("Cannot find the section " + sectionId + " in your diaries");

    }

    @Transactional
    public String likeSection(int userId, int sectionId) {
        boolean isMine = false;
        if(sectionRepository.findById(sectionId).isEmpty()){
            throw new BadRequestException("The section you are trying to like is invalid.");
        }

        Section section = sectionRepository.findById(sectionId).get();
        User user = userRepository.findById(userId).get();

        for (Diary d : user.getDiaries()) {
            for (Section s: d.getSections()) {
                if(s.getId() == sectionId){
                    isMine = true;
                }
            }
        }

        if(userDAO.hasUserLikedSection(user, section)){
            throw new BadRequestException("You already liked this section");
        }

        if((UtilSection.isVisible(section)) && (userDAO.isSectionSharedUser(section, user)
                || isMine)){

            if(section.getDisLikers().contains(user)){
                section.getDisLikers().remove(user);
                sectionRepository.save(section);
            }

            section.getLikers().add(user);
            sectionRepository.save(section);
            return "You liked the section with title \"" + section.getTitle() + "\"";
        }

        throw new NotFoundException("Cannot find the section");
    }

    @Transactional
    public String dislikeSection(int userId, int sectionId) {
        boolean isMine = false;
        if(sectionRepository.findById(sectionId).isEmpty()){
            throw new BadRequestException("The section you are trying to disLike is invalid.");
        }

        Section section = sectionRepository.findById(sectionId).get();
        User user = userRepository.findById(userId).get();

        for (Diary d : user.getDiaries()) {
            for (Section s: d.getSections()) {
                if(s.getId() == sectionId){
                    isMine = true;
                }
            }
        }

        if(userDAO.hasUserDisLikedSection(user, section)){
            throw new BadRequestException("You already disLiked this section");
        }

        if((UtilSection.isVisible(section)) && (userDAO.isSectionSharedUser(section, user)
                || isMine)){

            if(section.getLikers().contains(user)){
                section.getLikers().remove(user);
                sectionRepository.save(section);
            }

            section.getDisLikers().add(user);
            sectionRepository.save(section);
            return "You disLiked the section with title \"" + section.getTitle() + "\"";
        }

        throw new NotFoundException("Cannot find the section");
    }


    public String shareSection(int myId,int shareUserId, int sectionId) {
        Optional<Section> s = sectionRepository.findById(sectionId);
        if(!s.isPresent()){
            throw new NotFoundException("section not found");
        }
        Optional<User> u = userRepository.findById(shareUserId);
        if(!u.isPresent()){
            throw new NotFoundException("user not found");
        }

        if(sectionDbDAO.isAlreadyShared(sectionId,shareUserId)){
            throw new BadRequestException( "You already shared the section with this user");
        }

        if(myId == shareUserId){
            throw new BadRequestException("You cannot share section with yourself");
        }

        Section section = s.get();

        if(!UtilSection.isVisible(section)){
            throw new NotFoundException("secttion is not visible");
        }

        User user = u.get();
        User me = userRepository.findById(myId).get();

        for (Diary d : me.getDiaries()) {
            for (Section sec: d.getSections()) {
                if(sec.getId() == sectionId){
                    section.getUsersSharedWith().add(user);
                    sectionRepository.save(section);
                    return "You shared section with id : " + sectionId + " with user with id " + shareUserId;                }
            }
        }
        throw new NotFoundException("This section is not in your diaries");
    }

    public String unshareSection(int myId, int userId, int sectionId) {
        Optional<Section> s = sectionRepository.findById(sectionId);
        if(!s.isPresent()){
            throw new NotFoundException("section not found");
        }
        if(sectionDbDAO.isAlreadyShared(sectionId,userId)){
            User me = userRepository.findById(myId).get();

            for (Diary d : me.getDiaries()) {
                for (Section sec: d.getSections()) {
                    if(sec.getId() == sectionId){
                      return sectionDbDAO.removeShare(userId,sectionId);
                    }
                }
            }
        }
        throw new BadRequestException("You never share the section with this user");
    }

    public List<Section> getMySections(int userId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new BadRequestException("The user is invalid.");
        }
        return sectionDbDAO.getAllUserSections(userId);
    }

    public List<Section> showSharedSectionsWithUser(int userId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new BadRequestException("The user is invalid.");
        }
        return sectionDbDAO.getSharedWithUserSections(userId);
    }

    public String removeLike(int userId, int sectionId) {
       return sectionDbDAO.removeLike(userId,sectionId);
    }

    public String removeDislike(int userId, int sectionId) {
       return sectionDbDAO.removeDislike(userId,sectionId);
    }

    public Section getSection(int userId, int sectionId) {
        User user = userRepository.findById(userId).get();
        Optional<Section> s = sectionRepository.findById(sectionId);

        if(!s.isPresent()){
            throw new NotFoundException("Section not found.");
        }

        for (Diary d:user.getDiaries()) {
            for (Section sec:d.getSections()) {
                if(sec.getId() == sectionId){
                    return sec;
                }
            }
        }
        throw new NotFoundException("Cannot find this section in your diaries.");
    }
}
