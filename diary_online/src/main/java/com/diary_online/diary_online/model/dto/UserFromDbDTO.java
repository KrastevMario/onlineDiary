package com.diary_online.diary_online.model.dto;


import com.diary_online.diary_online.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserFromDbDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;

    public UserFromDbDTO(String firstName, String lastName, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    public UserFromDbDTO(User fullUser) {
        this.firstName = fullUser.getFirstName();
        this.lastName = fullUser.getLastName();
        this.email = fullUser.getEmail();
        this.username = fullUser.getUsername();
    }
}
