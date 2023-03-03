package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.DIARY_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.PET_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.STAMP_LIST_SIZE_ERROR;
import static com.dasibom.practice.exception.ErrorCode.STAMP_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.USER_NOT_FOUND;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryStamp;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiarySaveReqDto;
import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.repository.DiaryRepository;
import com.dasibom.practice.repository.PetRepository;
import com.dasibom.practice.repository.StampRepository;
import com.dasibom.practice.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;
    private final StampRepository stampRepository;

    @Override
    @Transactional
    public Diary save(DiarySaveReqDto requestDto) {

        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        int stampListSize = 3;
        List<Stamp> stamps = getStamps(requestDto);
        if (stamps.size() > stampListSize) {
            throw new CustomException(STAMP_LIST_SIZE_ERROR);
        }

        List<DiaryStamp> diaryStamps = getDiaryStamps(stamps);
        Pet pet = petRepository.findByPetName(requestDto.getPet().getPetName())
                .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        return getDiary(requestDto, user, diaryStamps, pet);
    }

    @Override
    @Transactional
    public DiaryDetailResDto getDetailedDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        return new DiaryDetailResDto(diary);
    }

    private Diary getDiary(DiarySaveReqDto requestDto, User user, List<DiaryStamp> diaryStamps, Pet pet) {
        Diary diary = Diary.createDiary(user, pet, requestDto.getTitle(), requestDto.getContent(), diaryStamps);
        diaryRepository.save(diary);
        return diary;
    }

    private List<DiaryStamp> getDiaryStamps(List<Stamp> stamps) {
        List<DiaryStamp> diaryStamps = new ArrayList<>();
        for (Stamp stamp : stamps) {
            DiaryStamp diarystamp = DiaryStamp.createDiaryStamp(stamp);
            diaryStamps.add(diarystamp);
        }
        return diaryStamps;
    }

    private List<Stamp> getStamps(DiarySaveReqDto requestDto) {
        List<Stamp> stamps = new ArrayList<>();
        for (Stamp stamp : requestDto.getStamps()) {
            Stamp byStampType = stampRepository.findByStampType(stamp.getStampType())
                    .orElseThrow(() -> new CustomException(STAMP_NOT_FOUND));
            stamps.add(byStampType);
        }
        return stamps;
    }

}
