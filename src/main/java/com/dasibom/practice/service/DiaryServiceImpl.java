package com.dasibom.practice.service;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryStamp;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.domain.dto.DiarySaveReqDto;
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
        Optional<User> user = userRepository.findByUsername("test");

        if (user.isPresent()) {
            int stampListSize = 3;
//        if (stamps.size() > stampListSize) {
//            TODO: exception handling
//        }

            List<Stamp> stamps = getStamps(requestDto);
            List<DiaryStamp> diaryStamps = getDiaryStamps(stamps);
            Optional<Pet> pet = petRepository.findByPetName(requestDto.getPet().getPetName());
            if (pet.isPresent()) {
                return getDiary(requestDto, user.get(), diaryStamps, pet.get());
            }
            return null;
        }
        return null;
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
            // TODO: exception handling
            Optional<Stamp> byStampType = stampRepository.findByStampType(stamp.getStampType());
            if (byStampType.isPresent()) {
                stamps.add(byStampType.get());
            }
        }
        return stamps;
    }

}
