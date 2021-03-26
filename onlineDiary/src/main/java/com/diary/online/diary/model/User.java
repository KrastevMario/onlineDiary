package com.diary.online.diary.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
}
