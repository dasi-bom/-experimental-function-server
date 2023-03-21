package com.dasibom.practice.repository.custom;

import com.dasibom.practice.dto.DiaryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomDiaryRepository {

    Slice<DiaryDto.SimpleResponse> getDiaryBriefInfoScroll(Long cursorId, DiaryDto.ReadCondition condition, Pageable pageable);

    List<DiaryDto.DetailResponse> getDiaryDetailList(DiaryDto.ReadCondition condition);

}
