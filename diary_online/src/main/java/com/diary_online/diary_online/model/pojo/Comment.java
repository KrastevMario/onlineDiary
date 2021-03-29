package com.diary_online.diary_online.model.pojo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference(value = "user-comment")
    private User commentOwner;

    @ManyToOne
    @JoinColumn(name="section_id")
    @JsonBackReference(value = "section-comment")
    private Section commentSection;
}
