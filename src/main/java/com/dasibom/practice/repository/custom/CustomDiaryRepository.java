package com.dasibom.practice.repository.custom;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.dto.DiaryBriefInfoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomDiaryRepository {

    Slice<DiaryBriefInfoDto> getDiaryBriefInfoScroll(Long cursorId, DiaryReadCondition condition, Pageable pageable);

}
