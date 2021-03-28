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
@Table(name = "diaries")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User owner;

    @OneToMany(mappedBy = "diary")
    @JsonManagedReference
    List<Section> sections;

}
