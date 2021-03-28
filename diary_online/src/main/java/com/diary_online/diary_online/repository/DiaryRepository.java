package com.diary_online.diary_online.repository;

import com.diary_online.diary_online.model.pojo.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
}
