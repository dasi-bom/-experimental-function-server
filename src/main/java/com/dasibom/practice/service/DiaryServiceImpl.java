package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.DIARY_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.PET_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.STAMP_LIST_SIZE_ERROR;
import static com.dasibom.practice.exception.ErrorCode.STAMP_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.USER_NOT_FOUND;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryStamp;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryBriefInfoDto;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiarySaveReqDto;
import com.dasibom.practice.dto.DiaryUpdateReqDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
        List<Stamp> stamps = extractStamps(requestDto.getStamps());
//        List<Stamp> stamps = extractStamps(requestDto);
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

    @Override
    @Transactional
    public Slice<DiaryBriefInfoDto> getDiaryList(Long cursor, DiaryReadCondition condition, Pageable pageRequest) {
        return diaryRepository.getDiaryBriefInfoScroll(cursor, condition, pageRequest);
    }

    @Override
    @Transactional
    public void update(Long diaryId, DiaryUpdateReqDto updateRequestDto) {

        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        // 누구의 일기인가요? 변경
        Pet pet = null;
        if (updateRequestDto.getPet() != null) {
            pet = petRepository.findByPetNameAndOwner(updateRequestDto.getPet().getPetName(), user)
                    .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        }

        // initialize oldStamps
        List<DiaryStamp> oldDiaryStamps = diary.getDiaryStamps();
        List<Stamp> oldStamps = new ArrayList<>();
        for (DiaryStamp oldDiaryStamp : oldDiaryStamps) {
            oldStamps.add(oldDiaryStamp.getStamp());
        }

        // 기존 스탬프 제거
        if (updateRequestDto.getStamps() != null) {
            if (!oldDiaryStamps.isEmpty()) {
                DiaryStamp.removeDiaryStamp(oldDiaryStamps);
                stampRepository.deleteAll(oldStamps);
            }
        }

        // 새로운 스탬프 생성
        int stampListSize = 3;
        List<DiaryStamp> newDiaryStamps = null;
        if (updateRequestDto.getStamps() != null) {
            if (updateRequestDto.getStamps().size() > stampListSize) {
                throw new CustomException(STAMP_LIST_SIZE_ERROR);
            }
            List<Stamp> newStamps = extractStamps(updateRequestDto.getStamps());
            newDiaryStamps = getDiaryStamps(newStamps);
        }

        // 일기 update
        diary.updateDiary(updateRequestDto.getTitle(), updateRequestDto.getContent(), newDiaryStamps, pet);

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

//    private List<Stamp> getStamps(DiarySaveReqDto requestDto) {
//        List<Stamp> stamps = new ArrayList<>();
//        for (Stamp stamp : requestDto.getStamps()) {
//            Stamp byStampType = stampRepository.findByStampType(stamp.getStampType())
//                    .orElseThrow(() -> new CustomException(STAMP_NOT_FOUND));
//            stamps.add(byStampType);
//        }
//        return stamps;
//    }

    private List<Stamp> extractStamps(List<Stamp> stamps) {
        List<Stamp> resStamps = new ArrayList<>();
        for (Stamp stamp : stamps) {
            Stamp byStampType = stampRepository.findByStampType(stamp.getStampType())
                    .orElseThrow(() -> new CustomException(STAMP_NOT_FOUND));
            resStamps.add(byStampType);
        }
        return resStamps;
    }

}
