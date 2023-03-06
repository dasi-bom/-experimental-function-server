package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.repository.custom.CustomDiaryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {

    // Entity 테이블을 id 역순으로 정렬한 뒤 첫 로우 리턴
    Diary findFirstByOrderByIdDesc();
}
