package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.DIARY_ALREADY_EXIST_ERROR;
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
import com.dasibom.practice.dto.DiaryBriefResDto;
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
import java.util.Optional;
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

    // diary ID 값 발급
    public Long issueId() {
        Diary lastDiary = diaryRepository.findFirstByOrderByIdDesc();
        if (lastDiary == null) {
            return (long) 1;
        }
        return (long) (lastDiary.getId() + 1);
    }

    @Override
    @Transactional
    public Diary save(Long diaryId, DiarySaveReqDto requestDto) {

        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if (diary.isPresent()) {
            throw new CustomException(DIARY_ALREADY_EXIST_ERROR);
        }

        int stampListSize = 3;
        List<Stamp> stamps = extractStamps(requestDto.getStamps());
        if (stamps.size() > stampListSize) {
            throw new CustomException(STAMP_LIST_SIZE_ERROR);
        }

        List<DiaryStamp> diaryStamps = makeDiaryStamps(stamps);
        Pet pet = petRepository.findByPetNameAndOwner(requestDto.getPet().getPetName(), user)
                .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        return getDiary(diaryId, requestDto, user, diaryStamps, pet);
    }

    @Override
    @Transactional
    public DiaryDetailResDto getDetailedDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if (diary.getIsDeleted()) {
            throw new CustomException(DIARY_NOT_FOUND);
        }
        return new DiaryDetailResDto(diary);
    }

    @Override
    @Transactional
    public Slice<DiaryBriefResDto> getDiaryList(Long cursor, DiaryReadCondition condition, Pageable pageRequest) {
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
        if (diary.getIsDeleted()) {
            throw new CustomException(DIARY_NOT_FOUND);
        }

        Pet pet = findPet(updateRequestDto.getPet(), user);
//        Pet pet = updatePet(updateRequestDto, user);
        List<DiaryStamp> newDiaryStamps = updateStamp(updateRequestDto, diary);
        diary.updateDiary(updateRequestDto.getTitle(), updateRequestDto.getContent(), newDiaryStamps, pet);
    }

    @Override
    @Transactional
    public Diary delete(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if (diary.getIsDeleted()) {
            throw new CustomException(DIARY_NOT_FOUND);
        }

        // 스탬프 제거
        List<DiaryStamp> diaryStamps = diary.getDiaryStamps();
        if (!diaryStamps.isEmpty()) {
            DiaryStamp.removeDiaryStamp(diaryStamps); // 해당 게시글의 DiaryStamp 목록에서 diaryStamp 삭제
        }

        return diary;
    }

    @Override
    @Transactional
    public List<DiaryDetailResDto> getAgainDiaryList(DiaryReadCondition condition) {
        return diaryRepository.getDiaryDetailList(condition);
    }

    // 누구의 일기인가요? 변경
    private Pet findPet(Pet reqPet , User user) {
        Pet pet = null;
        if (reqPet != null) {
            pet = petRepository.findByPetNameAndOwner(reqPet.getPetName(), user)
                    .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        }
        return pet;
    }
//    private Pet updatePet(DiaryUpdateReqDto updateRequestDto, User user) {
//        Pet pet = null;
//        if (updateRequestDto.getPet() != null) {
//            pet = petRepository.findByPetNameAndOwner(updateRequestDto.getPet().getPetName(), user)
//                    .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
//        }
//        return pet;
//    }

    private List<DiaryStamp> updateStamp(DiaryUpdateReqDto updateRequestDto, Diary diary) {
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
            newDiaryStamps = makeDiaryStamps(newStamps);
        }
        return newDiaryStamps;
    }

    private Diary getDiary(Long diaryId, DiarySaveReqDto requestDto, User user, List<DiaryStamp> diaryStamps, Pet pet) {
        Diary diary = Diary.createDiary(diaryId, user, pet, requestDto.getTitle(), requestDto.getContent(), diaryStamps);
        diaryRepository.save(diary);
        return diary;
    }

    private List<DiaryStamp> makeDiaryStamps(List<Stamp> stamps) {
        List<DiaryStamp> diaryStamps = new ArrayList<>();
        for (Stamp stamp : stamps) {
            DiaryStamp diarystamp = DiaryStamp.createDiaryStamp(stamp);
            diaryStamps.add(diarystamp);
        }
        return diaryStamps;
    }

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
