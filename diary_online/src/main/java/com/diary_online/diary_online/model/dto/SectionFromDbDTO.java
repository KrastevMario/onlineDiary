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
public class SectionFromDbDTO {
    private int id;
    private String title;
    private String content;
    private String privacy;
    private LocalDateTime createdAt;

    public SectionFromDbDTO(int id, String title, String content, String privacy, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.privacy = privacy;
        this.createdAt = createdAt;
    }

    public SectionFromDbDTO(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.content = section.getContent();
        this.privacy = section.getPrivacy();
        this.createdAt = section.getCreatedAt();
    }
}
