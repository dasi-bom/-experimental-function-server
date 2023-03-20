package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.repository.custom.CustomDiaryRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {

    // Entity 테이블을 id 역순으로 정렬한 뒤 첫 로우 리턴
    Diary findFirstByOrderByIdDesc();

    List<Diary> findDiariesByPetAndAuthor(Pet pet, User author);
}
