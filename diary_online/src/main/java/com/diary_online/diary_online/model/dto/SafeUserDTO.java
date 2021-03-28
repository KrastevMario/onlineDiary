package com.diary_online.diary_online.model.dto;

import com.diary_online.diary_online.model.pojo.Diary;
import com.diary_online.diary_online.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class SafeUserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private String username;
    List<DiaryWithOutOwnerDTO> diaries;

    public SafeUserDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.username = user.getUsername();

        diaries = new ArrayList<>();
        for (Diary d : user.getDiaries()) {
            diaries.add(new DiaryWithOutOwnerDTO(d));
        }
    }
}
