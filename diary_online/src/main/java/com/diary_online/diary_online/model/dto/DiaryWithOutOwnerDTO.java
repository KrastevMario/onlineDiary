package com.diary_online.diary_online.model.dto;

import com.diary_online.diary_online.model.pojo.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
@Component
public class DiaryWithOutOwnerDTO {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private int ownerId;

    public DiaryWithOutOwnerDTO(Diary diary){
        this.id = diary.getId();
        this.title = diary.getTitle();
        this.createdAt = diary.getCreatedAt();
        this.ownerId = diary.getOwner().getId();
    }
}
