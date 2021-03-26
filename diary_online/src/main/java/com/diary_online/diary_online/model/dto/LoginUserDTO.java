package com.diary_online.diary_online.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoginUserDTO {
    private String username;
    private String password;
}
