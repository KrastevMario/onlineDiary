package com.diary_online.diary_online.model.dto;

import com.diary_online.diary_online.model.pojo.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private int id;
    private String content;
    private LocalDateTime createdAt;
    private int sectionId;
    private int ownerId;

    public CommentDTO(Comment comment){
        id = comment.getId();
        content = comment.getContent();
        createdAt = comment.getCreatedAt();
        this.ownerId = comment.getCommentOwner().getId();
        this.sectionId = comment.getCommentSection().getId();
    }
}
