package com.diary_online.diary_online.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private String password;
    private String username;

    @OneToMany(mappedBy = "owner")
    @JsonManagedReference
    List<Diary> diaries;

    @ManyToMany(mappedBy = "likers")
    @JsonBackReference
    private List<Section> likedSections;

    @ManyToMany(mappedBy = "disLikers")
    @JsonBackReference
    private List<Section> dislikedSections;

    @ManyToMany(mappedBy = "usersSharedWith")
    @JsonBackReference
    private List<Section> sharedSections;


    @ManyToMany
    @JoinTable(
            name = "users_have_followers",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "following_user_id") }
    )
    @JsonManagedReference
    List<User> followers;


    @ManyToMany(mappedBy = "followers")
    @JsonBackReference
    private List<User> following;

    @OneToMany(mappedBy = "commentOwner")
   // @JsonManagedReference
    List<Comment> comments;
}
