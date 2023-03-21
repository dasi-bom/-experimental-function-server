package com.dasibom.practice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.StampType;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiaryDto;
import com.dasibom.practice.repository.DiaryRepository;
import com.dasibom.practice.repository.PetRepository;
import com.dasibom.practice.repository.RecordRepository;
import com.dasibom.practice.repository.StampRepository;
import com.dasibom.practice.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PetRepository petRepository;
    @Mock
    DiaryRepository diaryRepository;
    @Mock
    StampRepository stampRepository;
    @Mock
    RecordRepository recordRepository;

    @InjectMocks
    DiaryServiceImpl diaryService;

    private User createUser() {
        return User.builder()
                .id(1L)
                .username("test")
                .password("test123")
                .build();
    }

    private Pet createPet() {
        return Pet.builder()
                .id(1L)
                .owner(createUser())
                .petName("gomgom")
                .protectionStartedAt(LocalDate.of(2023,3,20))
                .build();
    }

    private Stamp createWalkStamp() {
        return Stamp.builder()
                .id(1L)
                .stampType(StampType.WALK)
                .build();
    }

    private Stamp createToyStamp() {
        return Stamp.builder()
                .id(2L)
                .stampType(StampType.TOY)
                .build();
    }

    private DiaryDto.SaveRequest createSaveRequest() {
        List<Stamp> stamps = new ArrayList<>();
        stamps.add(createWalkStamp());
        stamps.add(createToyStamp());
        return DiaryDto.SaveRequest.builder()
                .title("titleTest")
                .content("contentTest")
                .pet(createPet())
                .stamps(stamps)
                .build();
    }

    private Diary createDiary() {
        return createSaveRequest().toEntity();
    }

    @DisplayName("다이어리 ID를 발급한다.")
    @Test
    public void issueIdTest() {
        Long nextId = diaryService.issueId();
        assertThat(nextId).isSameAs(1L);
    }

    @DisplayName("일기 저장에 성공한다.")
    @Test
    public void saveTest() {
        User user = createUser();
        Pet pet = createPet();
        Stamp walkStamp = createWalkStamp();
        Stamp toyStamp = createToyStamp();
        DiaryDto.SaveRequest diary = createSaveRequest();

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(petRepository.findByPetNameAndOwner("gomgom",user)).thenReturn(Optional.of(pet));
        when(stampRepository.findByStampType(StampType.WALK)).thenReturn(Optional.of(walkStamp));
        when(stampRepository.findByStampType(StampType.TOY)).thenReturn(Optional.of(toyStamp));
        when(diaryRepository.findById(any())).thenReturn(Optional.empty());
        when(diaryRepository.save(any())).thenReturn(diary.toEntity());

        diaryService.save(1L, diary);

        assertThat(user.getDiaries().size()).isEqualTo(1);
        verify(userRepository, atLeastOnce()).findByUsername("test");
        verify(petRepository, atLeastOnce()).findByPetNameAndOwner("gomgom",user);
        verify(stampRepository, times(1)).findByStampType(StampType.WALK);
        verify(stampRepository, times(1)).findByStampType(StampType.TOY);
        verify(diaryRepository, atLeastOnce()).findById(any());
        verify(diaryRepository, atLeastOnce()).save(any());
    }

    @DisplayName("특정 ID를 가진 일기가 존재하여 조회에 성공한다.")
    @Test
    public void getDetailedDiaryTest() {
        Diary diary = createDiary();
        Long id = 1L;
        given(diaryRepository.findById(id)).willReturn(Optional.of(diary));

        DiaryDetailResDto diaryDetail = diaryService.getDetailedDiary(id);

        assertThat(diaryDetail.getTitle()).isEqualTo(diaryDetail.getTitle());
        assertThat(diaryDetail.getContent()).isEqualTo(diaryDetail.getContent());
        assertThat(diaryDetail.getPetName()).isEqualTo(diaryDetail.getPetName());
        assertThat(diaryDetail.getImgUrls()).isEqualTo(diaryDetail.getImgUrls());
        assertThat(diaryDetail.getStampTypes()).isEqualTo(diaryDetail.getStampTypes());
        assertThat(diaryDetail.getCreatedAt()).isEqualTo(diaryDetail.getCreatedAt());
        verify(diaryRepository, times(1)).findById(id);
    }

}