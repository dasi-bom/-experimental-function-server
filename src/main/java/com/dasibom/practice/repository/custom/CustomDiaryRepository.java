package com.dasibom.practice.repository.custom;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.dto.DiaryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomDiaryRepository {

    Slice<DiaryDto.SimpleResponse> getDiaryBriefInfoScroll(Long cursorId, DiaryReadCondition condition, Pageable pageable);

    List<DiaryDto.DetailResponse> getDiaryDetailList(DiaryReadCondition condition);

}
