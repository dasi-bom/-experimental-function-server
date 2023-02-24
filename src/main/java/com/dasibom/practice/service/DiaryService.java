package com.dasibom.practice.service;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.dto.DiarySaveReqDto;

public interface DiaryService {

    Diary save(DiarySaveReqDto requestDto);

}
