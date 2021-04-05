package com.diary_online.diary_online.model.dto;

import com.diary_online.diary_online.model.pojo.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Getter
@Setter
@NoArgsConstructor

public class ReactedSectionDTO {
    private int id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String privacy;
    private int diaryId;
    private int likes;
    private int dislikes;

    public ReactedSectionDTO(Section s,int likes,int dislikes){
        id = s.getId();
        title = s.getTitle();
        content = s.getContent();
        createdAt = s.getCreatedAt();
        privacy = s.getPrivacy();
        diaryId = s.getDiary().getId();
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
