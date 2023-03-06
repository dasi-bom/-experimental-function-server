package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.repository.custom.CustomDiaryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {

    Diary findFirstByOrderByIdDesc();
}
