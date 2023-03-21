package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Stamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DiaryDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveRequest {
        @NotNull(message = "대상 동물은 필수 선택 값입니다.")
        private Pet pet;

        @NotBlank(message = "제목은 필수 입력 값입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력 값입니다.")
        private String content;

        private List<Stamp> stamps = new ArrayList<>();
//        private final List<Stamp> stamps = new ArrayList<>();

        @Builder
        public SaveRequest(Pet pet, String title, String content, List<Stamp> stamps) {
            this.pet = pet;
            this.title = title;
            this.content = content;
            this.stamps = stamps;
        }

        public Diary toEntity() {
            return Diary.builder()
                    .content(this.content)
                    .title(this.title)
                    .pet(this.pet)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {
        private Pet pet = null;
        private String title;
        private String content;
        private List<Stamp> stamps = null;
    }



}
