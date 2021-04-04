package com.diary_online.diary_online.repository;

import com.diary_online.diary_online.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
