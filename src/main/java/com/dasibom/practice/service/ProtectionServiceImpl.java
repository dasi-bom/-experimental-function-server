package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.DIARY_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.PET_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.USER_NOT_FOUND;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryStamp;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Record;
import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.StampType;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.ProtectionEndReqDto;
import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.repository.DiaryRepository;
import com.dasibom.practice.repository.PetRepository;
import com.dasibom.practice.repository.RecordRepository;
import com.dasibom.practice.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProtectionServiceImpl implements ProtectionService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    private final DiaryRepository diaryRepository;

    private final RecordRepository recordRepository;

    @Override
    @Transactional
    public void saveEndStatus(ProtectionEndReqDto protectionEndReqDto) {
        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 임보 종료일 update
        Pet pet = findPet(protectionEndReqDto.getPet(), user);
        pet.updateProtectionEndedAt();

        // 다시보기 Record 생성
        List<Diary> tmpDiary = diaryRepository.findDiariesByPetAndAuthor(pet, user);

        List<Diary> existDiaries = tmpDiary.stream()
                .filter(diary1 -> !diary1.getIsDeleted())
                .collect(Collectors.toList());
        Collections.shuffle(existDiaries); // 랜덤 셔플


        List<Diary> walkDiaries = getDiaries(existDiaries, StampType.WALK);
        Record record = Record.createRecord(pet,StampType.WALK, walkDiaries);
        recordRepository.save(record);


        List<Diary> treatDiaries = getDiaries(existDiaries, StampType.TREAT);
        Record record2 = Record.createRecord(pet,StampType.TREAT, treatDiaries);
        recordRepository.save(record2);


        List<Diary> toyDiaries = getDiaries(existDiaries, StampType.TOY);
        Record record3 = Record.createRecord(pet,StampType.TOY, toyDiaries);
        recordRepository.save(record3);
    }

    private List<Diary> getDiaries(List<Diary> existDiaries, StampType stampType) {

        List<Diary> targetDiaries = existDiaries.stream()
                .filter(diary -> diary.getRecord() == null) // 한 일기에 스탬프 2개 이상 찍혔을 때 중복 방지
                .collect(Collectors.toList());

        int maxDiarySize = 3;
        List<Diary> diaries = new ArrayList<>();
        for (Diary diary : targetDiaries) {

            if (diaries.size() == maxDiarySize) {
                break;
            }

            List<StampType> stampTypeList = diary.getDiaryStamps()
                    .stream()
                    .map(diaryStamp -> diaryStamp.getStamp().getStampType())
                    .collect(Collectors.toList());

            if (stampTypeList.contains(stampType)) {
                diaries.add(diary);
            }
        }
        return diaries;
    }

    private Pet findPet(Pet reqPet, User user) {
        Pet pet = null;
        if (reqPet != null) {
            pet = petRepository.findByPetNameAndOwner(reqPet.getPetName(), user)
                    .orElseThrow(() -> new CustomException(PET_NOT_FOUND));
        }
        return pet;
    }
}
