package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryImage;
import com.dasibom.practice.domain.DiaryStamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiaryDetailResDto {

    private final String title;
    private final String content;
    private final String petName;
    private final List<String> imgUrls = new ArrayList<>();
    private final List<String> stampTypes = new ArrayList<>();
    private final LocalDate createdAt;

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
