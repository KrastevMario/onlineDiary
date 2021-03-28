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
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name="diary_id")
    @JsonBackReference
    private Diary diary;

    @ManyToMany
    @JoinTable(
            name = "sections_have_likes",
            joinColumns = { @JoinColumn(name = "section_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @JsonManagedReference
    List<User> likers;

    @ManyToMany
    @JoinTable(
            name = "sections_have_dislikes",
            joinColumns = { @JoinColumn(name = "section_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @JsonManagedReference
    List<User> disLikers;

    @ManyToMany
    @JoinTable(
            name = "shared_sections",
            joinColumns = { @JoinColumn(name = "section_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @JsonManagedReference
    List<User> usersSharedWith;

    @OneToMany(mappedBy = "commentSection")
  //  @JsonManagedReference
    List<Comment> comments;
}
