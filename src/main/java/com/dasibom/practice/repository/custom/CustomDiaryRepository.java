package com.dasibom.practice.repository.custom;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.dto.DiaryBriefResDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomDiaryRepository {

    Slice<DiaryBriefResDto> getDiaryBriefInfoScroll(Long cursorId, DiaryReadCondition condition, Pageable pageable);

}
