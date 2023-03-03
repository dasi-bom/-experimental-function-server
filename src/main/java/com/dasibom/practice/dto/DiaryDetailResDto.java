package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryImage;
import com.dasibom.practice.domain.DiaryStamp;
import com.dasibom.practice.domain.Stamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DiaryDetailResDto {

    private String title;
    private String content;
    private String petName;
    private List<String> imgUrls = new ArrayList<>();
    private List<String> stampTypes = new ArrayList<>();
    private LocalDate createdAt;

    public DiaryDetailResDto(Diary entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.petName = entity.getPet().getPetName();
        this.createdAt = entity.getCreatedAt();
        initImgUrls(entity);
        initStampTypes(entity);
    }

    private void initImgUrls(Diary entity) {
        for (DiaryImage image : entity.getImages()) {
            this.imgUrls.add(image.getImgUrl());
        }
    }

    private void initStampTypes(Diary entity) {
        for (DiaryStamp diaryStamp : entity.getStamps()) {
            this.stampTypes.add(diaryStamp.getStamp().getStampType().toString());
        }
    }
}
