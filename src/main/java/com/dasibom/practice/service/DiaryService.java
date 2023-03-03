package com.dasibom.practice.service;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiarySaveReqDto;

public interface DiaryService {

    Diary save(DiarySaveReqDto requestDto);
    DiaryDetailResDto getDetailedDiary(Long diaryId);

}
